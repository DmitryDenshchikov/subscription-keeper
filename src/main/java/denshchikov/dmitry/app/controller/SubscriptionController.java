package denshchikov.dmitry.app.controller;

import denshchikov.dmitry.app.constant.MediaType;
import denshchikov.dmitry.app.model.domain.Subscription;
import denshchikov.dmitry.app.model.request.CreateSubscriptionRequest;
import denshchikov.dmitry.app.model.request.ReactivateSubscriptionRequest;
import denshchikov.dmitry.app.model.request.EndSubscriptionRequest;
import denshchikov.dmitry.app.model.response.common.ErrorResponse;
import denshchikov.dmitry.app.model.response.common.SuccessResponse;
import denshchikov.dmitry.app.model.response.subscription.*;
import denshchikov.dmitry.app.service.SubscriptionService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

import static denshchikov.dmitry.app.constant.MediaType.*;
import static denshchikov.dmitry.app.util.DateUtils.toUTC;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription data"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content()),
            @ApiResponse(
                    responseCode = "500",
                    content = {
                            @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class))}
            )
    })
    @PostMapping(consumes = CREATING_SUBSCRIPTION, produces = APPLICATION_JSON_VALUE)
    public SuccessResponse<SubscriptionCreatedResponse> storeSubscription(@RequestBody @Valid CreateSubscriptionRequest req) {
        Subscription subscription = subscriptionService.createSubscription(req.getUserId(), req.getStartDateTime());

        SubscriptionCreatedResponse response = new SubscriptionCreatedResponse(
                subscription.getId(),
                subscription.getUserId(),
                toUTC(subscription.getStartedOn())
        );

        return new SuccessResponse<>(response);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription status"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content()),
            @ApiResponse(
                    responseCode = "500",
                    content = {
                            @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class))}
            )
    })
    @GetMapping(path = "/users/{userId}", produces = APPLICATION_JSON_VALUE)
    public SuccessResponse<SubscriptionStatusResponse> getSubscriptionStatus(@PathVariable("userId") UUID userId) {
        boolean isSubscribed = subscriptionService.isSubscribed(userId);

        SubscriptionStatusResponse response = new SubscriptionStatusResponse(
                isSubscribed ? SubscriptionStatus.SUBSCRIBED : SubscriptionStatus.UNSUBSCRIBED);

        return new SuccessResponse<>(response);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription data"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content()),
            @ApiResponse(
                    responseCode = "500",
                    content = {
                            @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class))}
            )
    })
    @PatchMapping(path = "/users/{userId}", consumes = REACTIVATING_SUBSCRIPTION, produces = APPLICATION_JSON_VALUE)
    public SuccessResponse<SubscriptionReactivatedResponse> resubscribeUser(@PathVariable("userId") UUID userId,
                                                                            @RequestBody @Valid ReactivateSubscriptionRequest req) {
        Subscription subscription = subscriptionService.resubscribeUser(userId, req.getStartDateTime());

        SubscriptionReactivatedResponse response = new SubscriptionReactivatedResponse(
                subscription.getId(),
                subscription.getUserId(),
                toUTC(subscription.getStartedOn())
        );

        return new SuccessResponse<>(response);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription data"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content()),
            @ApiResponse(
                    responseCode = "500",
                    content = {
                            @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class))}
            )
    })
    @PatchMapping(path = "/users/{userId}", consumes = ENDING_SUBSCRIPTION, produces = APPLICATION_JSON_VALUE)
    public SuccessResponse<SubscriptionEndedResponse> endSubscription(@PathVariable("userId") UUID userId,
                                                                      @RequestBody @Valid EndSubscriptionRequest req) {
        Subscription subscription = subscriptionService.endSubscription(userId, req.getEndDateTime());

        SubscriptionEndedResponse response = new SubscriptionEndedResponse(
                subscription.getId(), subscription.getUserId(),
                toUTC(subscription.getStartedOn()),
                toUTC(subscription.getEndedOn())
        );

        return new SuccessResponse<>(response);
    }

}
