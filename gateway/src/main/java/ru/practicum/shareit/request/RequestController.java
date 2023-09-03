package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.request.dto.RequestDtoIn;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class RequestController {

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestBody @Valid RequestDtoIn dto,
                                             @RequestHeader(USER_ID_HEADER) long userId) {
        return requestClient.addRequest(dto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getRequests(@RequestHeader(USER_ID_HEADER) long userId) {
        return requestClient.getRequests(userId);
    }

    @GetMapping("all")
    public ResponseEntity<Object> getRequestsAll(@RequestHeader(USER_ID_HEADER) long userId,
                                                 @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                 @RequestParam(defaultValue = "20") @Positive int size) {
        return requestClient.getRequestsAll(userId, from, size);
    }

    @GetMapping("{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader(USER_ID_HEADER) long userId,
                                                 @PathVariable("requestId") long requestId) {
        return requestClient.getRequestById(userId, requestId);
    }
}
