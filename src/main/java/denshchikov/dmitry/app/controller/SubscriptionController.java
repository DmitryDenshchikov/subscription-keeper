package denshchikov.dmitry.app.controller;

import denshchikov.dmitry.app.model.domain.Subscription;
import denshchikov.dmitry.app.model.request.CreateSubscriptionRequest;
import denshchikov.dmitry.app.model.request.UpdateSubscriptionRequest;
import denshchikov.dmitry.app.model.response.common.ErrorResponse;
import denshchikov.dmitry.app.model.response.common.SuccessResponse;
import denshchikov.dmitry.app.model.response.subscription.*;
import denshchikov.dmitry.app.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

import static denshchikov.dmitry.app.constant.MediaType.*;
import static denshchikov.dmitry.app.model.response.subscription.SubscriptionStatus.SUBSCRIBED;
import static denshchikov.dmitry.app.model.response.subscription.SubscriptionStatus.UNSUBSCRIBED;
import static denshchikov.dmitry.app.util.DateUtils.toUTC;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/v1/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @Operation(summary = "Store a new subscription (user was subscribed)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription data"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content()),
            @ApiResponse(responseCode = "500", content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping(consumes = CREATE_SUBSCRIPTION, produces = SUBSCRIPTION)
    public SuccessResponse<SubscriptionResponse> storeSubscription(@RequestBody @Valid CreateSubscriptionRequest req) {
        Subscription subscription = subscriptionService.createSubscription(req.getUserId(), req.getStartDateTime());

        SubscriptionResponse response = SubscriptionResponse.builder()
                .id(subscription.getId())
                .userId(subscription.getUserId())
                .startedOn(toUTC(subscription.getStartedOn()))
                .build();

        return new SuccessResponse<>(response);
    }

    @Operation(summary = "Get a subscription's status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription status"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content()),
            @ApiResponse(responseCode = "500", content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping(path = "/users/{userId}", produces = SUBSCRIPTION_STATUS)
    public SuccessResponse<SubscriptionStatusResponse> getSubscriptionStatus(@PathVariable("userId") UUID userId) {
        boolean isSubscribed = subscriptionService.isSubscribed(userId);

        SubscriptionStatusResponse response = SubscriptionStatusResponse.builder()
                .subscriptionStatus(isSubscribed ? SUBSCRIBED : UNSUBSCRIBED)
                .build();

        return new SuccessResponse<>(response);
    }

    @Operation(summary = "End a subscription (a user was unsubscribed) or reactivate it (a user had been unsubscribed but then was subscribed again)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription data"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content()),
            @ApiResponse(responseCode = "500", content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PatchMapping(path = "/users/{userId}", consumes = UPDATE_SUBSCRIPTION, produces = SUBSCRIPTION)
    public SuccessResponse<SubscriptionResponse> updateSubscription(@PathVariable("userId") UUID userId,
                                                                    @RequestBody @Valid UpdateSubscriptionRequest req) {

        Subscription subscription = subscriptionService.updateSubscription(
                userId, req.getStartDateTime(), req.getEndDateTime());

        LocalDateTime subscriptionStartDateTime = subscription.getStartedOn();
        LocalDateTime subscriptionEndDateTime = subscription.getEndedOn();

        SubscriptionResponse response = SubscriptionResponse.builder()
                .id(subscription.getId())
                .userId(subscription.getUserId())
                .startedOn(subscriptionStartDateTime == null ? null : toUTC(subscriptionStartDateTime))
                .endedOn(subscriptionEndDateTime == null ? null : toUTC(subscriptionEndDateTime))
                .build();

        return new SuccessResponse<>(response);
    }

}
