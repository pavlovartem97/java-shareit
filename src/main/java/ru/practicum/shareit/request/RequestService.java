package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.request.dto.RequestDtoIn;
import ru.practicum.shareit.request.dto.RequestDtoOut;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.shareit.util.Utils.checkFromAndSize;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final RequestMapper requestMapper;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Transactional
    public RequestDtoOut addRequest(RequestDtoIn dto, long userId) {
        User requestor = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User is not found"));

        Request request = requestMapper.map(dto, requestor);
        request = requestRepository.save(request);
        return requestMapper.map(request);
    }

    @Transactional(readOnly = true)
    public List<RequestDtoOut> getRequests(long userId) {
        User requestor = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User is not found"));

        Collection<Request> requests = requestRepository.findByRequestorOrderByCreated(requestor);
        return mapRequestsWithItems(requests);
    }

    public List<RequestDtoOut> getRequestsAll(long userId, int from, int size) {
        checkFromAndSize(from, size);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User is not found"));

        Collection<Request> requests = requestRepository.findRequestsAll(user.getId(), from, size);
        return mapRequestsWithItems(requests);
    }

    public RequestDtoOut getRequestById(long userId, long requestId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User is not found"));
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request is not found"));
        return mapRequestsWithItems(List.of(request)).get(0);
    }

    private List<RequestDtoOut> mapRequestsWithItems(Collection<Request> requests) {
        if (requests.isEmpty()) {
            return List.of();
        }

        Collection<Item> items = itemRepository.findByRequestIn(requests);
        Map<Long, List<ItemDtoOut>> itemsByRequestId = items.stream().collect(Collectors.groupingBy(item -> item.getRequest().getId(),
                Collectors.mapping(itemMapper::map, Collectors.toUnmodifiableList())));

        return requests.stream().map(request -> requestMapper.map(request, itemsByRequestId.getOrDefault(request.getId(), List.of())))
                .collect(Collectors.toUnmodifiableList());
    }
}
