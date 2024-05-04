package denshchikov.dmitry.app.controller;

import denshchikov.dmitry.app.model.domain.Subscription;
import denshchikov.dmitry.app.model.request.CreateSubscriptionRequest;
import denshchikov.dmitry.app.model.request.ResubscribeUserRequest;
import denshchikov.dmitry.app.model.request.UnsubscribeUserRequest;
import denshchikov.dmitry.app.model.response.SubscriptionResponse;
import denshchikov.dmitry.app.model.response.SubscriptionStatus;
import denshchikov.dmitry.app.model.response.SubscriptionStatusResponse;
import denshchikov.dmitry.app.model.response.common.SuccessResponse;
import denshchikov.dmitry.app.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;


    @PostMapping
    public SuccessResponse<SubscriptionResponse> storeSubscription(@RequestBody CreateSubscriptionRequest req) {
        Subscription subscription = subscriptionService.createSubscriptionAndUser(req.getUserId(), req.getStartDateTime());

        SubscriptionResponse response = new SubscriptionResponse(
                subscription.getId(), subscription.getUserId(), subscription.getStartedOn(), subscription.getEndedOn()
        );

        return new SuccessResponse<>(response);
    }

    @GetMapping("/users/{userId}")
    public SuccessResponse<SubscriptionStatusResponse> getSubscriptionStatus(@PathVariable("userId") UUID userId) {
        boolean isSubscribed = subscriptionService.isSubscribed(userId);

        SubscriptionStatusResponse response = new SubscriptionStatusResponse(
                isSubscribed ? SubscriptionStatus.SUBSCRIBED : SubscriptionStatus.UNSUBSCRIBED);

        return new SuccessResponse<>(response);
    }

    @PutMapping("/users/{userId}")
    public SuccessResponse<SubscriptionResponse> resubscribeUser(@PathVariable("userId") UUID userId,
                                                                 @RequestBody ResubscribeUserRequest req) {
        Subscription subscription = subscriptionService.resubscribeUser(userId, req.getStartDateTime());

        SubscriptionResponse response = new SubscriptionResponse(
                subscription.getId(), subscription.getUserId(), subscription.getStartedOn(), subscription.getEndedOn()
        );

        return new SuccessResponse<>(response);
    }


    @DeleteMapping("/users/{userId}")
    public SuccessResponse<SubscriptionResponse> removeSubscription(@PathVariable("userId") UUID userId,
                                                                    @RequestBody UnsubscribeUserRequest req) {
        Subscription subscription = subscriptionService.unsubscribeUser(userId, req.getEndDateTime());

        SubscriptionResponse response = new SubscriptionResponse(
                subscription.getId(), subscription.getUserId(), subscription.getStartedOn(), subscription.getEndedOn()
        );

        return new SuccessResponse<>(response);
    }


}
