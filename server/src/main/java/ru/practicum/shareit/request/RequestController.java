package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.RequestDtoIn;
import ru.practicum.shareit.request.dto.RequestDtoOut;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class RequestController {

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    private final RequestService requestService;

    @PostMapping
    public RequestDtoOut addRequest(@RequestBody RequestDtoIn dto,
                                    @RequestHeader(USER_ID_HEADER) long userId) {
        return requestService.addRequest(dto, userId);
    }

    @GetMapping
    public List<RequestDtoOut> getRequests(@RequestHeader(USER_ID_HEADER) long userId) {
        return requestService.getRequests(userId);
    }

    @GetMapping("all")
    public List<RequestDtoOut> getRequestsAll(@RequestHeader(USER_ID_HEADER) long userId,
                                              @RequestParam(defaultValue = "0") int from,
                                              @RequestParam(defaultValue = "20") int size) {
        return requestService.getRequestsAll(userId, from, size);
    }

    @GetMapping("{requestId}")
    public RequestDtoOut getRequestById(@RequestHeader(USER_ID_HEADER) long userId,
                                        @PathVariable("requestId") long requestId) {
        return requestService.getRequestById(userId, requestId);
    }
}
