package denshchikov.dmitry.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import denshchikov.dmitry.app.model.domain.Subscription;
import denshchikov.dmitry.app.model.request.CreateSubscriptionRequest;
import denshchikov.dmitry.app.model.request.UpdateSubscriptionRequest;
import denshchikov.dmitry.app.service.SubscriptionService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.ZonedDateTime;
import java.util.UUID;

import static denshchikov.dmitry.app.constant.MediaType.*;
import static denshchikov.dmitry.app.util.DateUtils.toUTC;
import static java.time.ZoneOffset.UTC;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class SubscriptionControllerApiTest {

    @MockBean
    SubscriptionService subscriptionService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;

    @Nested
    public class Returns200 {

        @Test
        void should_CreateSubscription_When_StartedDateTimeIsZonedAndNoErrorsOccurred() throws Exception {
            // Given
            UUID userId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
            ZonedDateTime subscriptionStartDateTime = ZonedDateTime.parse("2024-05-05T16:29:16.513+03:00");

            CreateSubscriptionRequest request = new CreateSubscriptionRequest(userId, subscriptionStartDateTime);

            String requestStr = objectMapper.writeValueAsString(request);

            MockHttpServletRequestBuilder requestBuilder = post("/v1/subscriptions")
                    .accept(SUBSCRIPTION)
                    .contentType(CREATE_SUBSCRIPTION)
                    .content(requestStr);

            UUID subscriptionId = UUID.fromString("ef94bdff-8d32-432a-9444-95890e4b97bd");

            Subscription subscription = new Subscription();
            subscription.setId(subscriptionId);
            subscription.setUserId(userId);
            subscription.setStartedOn(toUTC(subscriptionStartDateTime));

            given(subscriptionService.createSubscription(userId, subscriptionStartDateTime)).willReturn(subscription);

            // When & Then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(SUBSCRIPTION))
                    .andExpect(content().json(
                            "{\n" +
                                    "  \"data\": {\n" +
                                    "    \"id\": \"ef94bdff-8d32-432a-9444-95890e4b97bd\",\n" +
                                    "    \"userId\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\n" +
                                    "    \"startedOn\": \"2024-05-05T13:29:16.513Z\"\n" +
                                    "  }\n" +
                                    "}"
                    ));
        }

        @Test
        void should_CreateSubscription_When_StartedDateTimeInUtcAndNoErrorsOccurred() throws Exception {
            // Given
            UUID userId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
            ZonedDateTime subscriptionStartDateTime =
                    ZonedDateTime.parse("2024-05-05T16:29:16.513+03:00").withZoneSameInstant(UTC);

            CreateSubscriptionRequest request = new CreateSubscriptionRequest(userId, subscriptionStartDateTime);

            String requestStr = objectMapper.writeValueAsString(request);

            MockHttpServletRequestBuilder requestBuilder = post("/v1/subscriptions")
                    .accept(SUBSCRIPTION)
                    .contentType(CREATE_SUBSCRIPTION)
                    .content(requestStr);

            UUID subscriptionId = UUID.fromString("ef94bdff-8d32-432a-9444-95890e4b97bd");

            Subscription subscription = new Subscription();
            subscription.setId(subscriptionId);
            subscription.setUserId(userId);
            subscription.setStartedOn(toUTC(subscriptionStartDateTime));

            given(subscriptionService.createSubscription(userId, subscriptionStartDateTime)).willReturn(subscription);

            // When & Then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(SUBSCRIPTION))
                    .andExpect(content().json(
                            "{\n" +
                                    "  \"data\": {\n" +
                                    "    \"id\": \"ef94bdff-8d32-432a-9444-95890e4b97bd\",\n" +
                                    "    \"userId\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\n" +
                                    "    \"startedOn\": \"2024-05-05T13:29:16.513Z\"\n" +
                                    "  }\n" +
                                    "}"
                    ));
        }

        @Test
        void should_ReturnSubscriptionStatus_When_ThereIsActiveSubscriptionErrorsOccurred() throws Exception {
            // Given
            UUID userId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");

            MockHttpServletRequestBuilder requestBuilder = get("/v1/subscriptions/users/" + userId)
                    .accept(SUBSCRIPTION_STATUS);

            given(subscriptionService.isSubscribed(userId)).willReturn(true);

            // When & Then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(SUBSCRIPTION_STATUS))
                    .andExpect(content().json(
                            "{\n" +
                                    "  \"data\": {\n" +
                                    "    \"subscriptionStatus\": \"SUBSCRIBED\"\n" +
                                    "  }\n" +
                                    "}"
                    ));
        }

        @Test
        void should_ReturnSubscriptionStatus_When_ThereIsNoActiveSubscriptionErrorsOccurred() throws Exception {
            // Given
            UUID userId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");

            MockHttpServletRequestBuilder requestBuilder = get("/v1/subscriptions/users/" + userId)
                    .accept(SUBSCRIPTION_STATUS);

            given(subscriptionService.isSubscribed(userId)).willReturn(false);

            // When & Then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(SUBSCRIPTION_STATUS))
                    .andExpect(content().json(
                            "{\n" +
                                    "  \"data\": {\n" +
                                    "    \"subscriptionStatus\": \"UNSUBSCRIBED\"\n" +
                                    "  }\n" +
                                    "}"
                    ));
        }

        @Test
        void should_ResubscribeUser_When_StartedDateTimeIsZonedAndNoErrorsOccurred() throws Exception {
            // Given
            UUID userId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
            ZonedDateTime subscriptionStartDateTime = ZonedDateTime.parse("2024-05-05T16:29:16.513+03:00");

            UpdateSubscriptionRequest request = new UpdateSubscriptionRequest(subscriptionStartDateTime, null);

            String requestStr = objectMapper.writeValueAsString(request);

            MockHttpServletRequestBuilder requestBuilder = patch("/v1/subscriptions/users/" + userId)
                    .accept(SUBSCRIPTION)
                    .contentType(UPDATE_SUBSCRIPTION)
                    .content(requestStr);

            UUID subscriptionId = UUID.fromString("ef94bdff-8d32-432a-9444-95890e4b97bd");

            Subscription subscription = new Subscription();
            subscription.setId(subscriptionId);
            subscription.setUserId(userId);
            subscription.setStartedOn(toUTC(subscriptionStartDateTime));

            given(subscriptionService.updateSubscription(userId, subscriptionStartDateTime, null)).willReturn(subscription);

            // When & Then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(SUBSCRIPTION))
                    .andExpect(content().json(
                            "{\n" +
                                    "  \"data\": {\n" +
                                    "    \"id\": \"ef94bdff-8d32-432a-9444-95890e4b97bd\",\n" +
                                    "    \"userId\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\n" +
                                    "    \"startedOn\": \"2024-05-05T13:29:16.513Z\"\n" +
                                    "  }\n" +
                                    "}"
                    ));
        }

        @Test
        void should_ResubscribeUser_When_StartDateTimeInUtcAndNoErrorsOccurred() throws Exception {
            // Given
            UUID userId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
            ZonedDateTime subscriptionStartDateTime = ZonedDateTime.parse("2024-05-05T16:29:16.513+03:00")
                    .withZoneSameInstant(UTC);

            UpdateSubscriptionRequest request = new UpdateSubscriptionRequest(subscriptionStartDateTime, null);

            String requestStr = objectMapper.writeValueAsString(request);

            MockHttpServletRequestBuilder requestBuilder = patch("/v1/subscriptions/users/" + userId)
                    .accept(SUBSCRIPTION)
                    .contentType(UPDATE_SUBSCRIPTION)
                    .content(requestStr);

            UUID subscriptionId = UUID.fromString("ef94bdff-8d32-432a-9444-95890e4b97bd");

            Subscription subscription = new Subscription();
            subscription.setId(subscriptionId);
            subscription.setUserId(userId);
            subscription.setStartedOn(toUTC(subscriptionStartDateTime));

            given(subscriptionService.updateSubscription(userId, subscriptionStartDateTime, null)).willReturn(subscription);

            // When & Then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(SUBSCRIPTION))
                    .andExpect(content().json(
                            "{\n" +
                                    "  \"data\": {\n" +
                                    "    \"id\": \"ef94bdff-8d32-432a-9444-95890e4b97bd\",\n" +
                                    "    \"userId\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\n" +
                                    "    \"startedOn\": \"2024-05-05T13:29:16.513Z\"\n" +
                                    "  }\n" +
                                    "}"
                    ));
        }

        @Test
        void should_EndSubscription_When_StartAndEndDateTimeAreZonedAndNoErrorsOccurred() throws Exception {
            // Given
            UUID userId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
            ZonedDateTime subscriptionStartDateTime = ZonedDateTime.parse("2024-05-05T16:29:16.513+03:00");
            ZonedDateTime subscriptionEndDateTime = ZonedDateTime.parse("2024-05-05T17:29:16.513+03:00");

            UpdateSubscriptionRequest request = new UpdateSubscriptionRequest(null, subscriptionEndDateTime);

            String requestStr = objectMapper.writeValueAsString(request);

            MockHttpServletRequestBuilder requestBuilder = patch("/v1/subscriptions/users/" + userId)
                    .accept(SUBSCRIPTION)
                    .contentType(UPDATE_SUBSCRIPTION)
                    .content(requestStr);

            UUID subscriptionId = UUID.fromString("ef94bdff-8d32-432a-9444-95890e4b97bd");

            Subscription subscription = new Subscription();
            subscription.setId(subscriptionId);
            subscription.setUserId(userId);
            subscription.setStartedOn(toUTC(subscriptionStartDateTime));
            subscription.setEndedOn(toUTC(subscriptionEndDateTime));

            given(subscriptionService.updateSubscription(userId, null, subscriptionEndDateTime)).willReturn(subscription);

            // When & Then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(SUBSCRIPTION))
                    .andExpect(content().json(
                            "{\n" +
                                    "  \"data\": {\n" +
                                    "    \"id\": \"ef94bdff-8d32-432a-9444-95890e4b97bd\",\n" +
                                    "    \"userId\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\n" +
                                    "    \"startedOn\": \"2024-05-05T13:29:16.513Z\",\n" +
                                    "    \"endedOn\": \"2024-05-05T14:29:16.513Z\"\n" +
                                    "  }\n" +
                                    "}"
                    ));
        }

        @Test
        void should_EndSubscription_When_StartAndEndDateTimeInUtcAndNoErrorsOccurred() throws Exception {
            // Given
            UUID userId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
            ZonedDateTime subscriptionStartDateTime = ZonedDateTime.parse("2024-05-05T16:29:16.513+03:00")
                    .withZoneSameInstant(UTC);
            ZonedDateTime subscriptionEndDateTime = ZonedDateTime.parse("2024-05-05T17:29:16.513+03:00")
                    .withZoneSameInstant(UTC);

            UpdateSubscriptionRequest request = new UpdateSubscriptionRequest(null, subscriptionEndDateTime);

            String requestStr = objectMapper.writeValueAsString(request);

            MockHttpServletRequestBuilder requestBuilder = patch("/v1/subscriptions/users/" + userId)
                    .accept(SUBSCRIPTION)
                    .contentType(UPDATE_SUBSCRIPTION)
                    .content(requestStr);

            UUID subscriptionId = UUID.fromString("ef94bdff-8d32-432a-9444-95890e4b97bd");

            Subscription subscription = new Subscription();
            subscription.setId(subscriptionId);
            subscription.setUserId(userId);
            subscription.setStartedOn(toUTC(subscriptionStartDateTime));
            subscription.setEndedOn(toUTC(subscriptionEndDateTime));

            given(subscriptionService.updateSubscription(userId, null, subscriptionEndDateTime)).willReturn(subscription);

            // When & Then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(SUBSCRIPTION))
                    .andExpect(content().json(
                            "{\n" +
                                    "  \"data\": {\n" +
                                    "    \"id\": \"ef94bdff-8d32-432a-9444-95890e4b97bd\",\n" +
                                    "    \"userId\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\n" +
                                    "    \"startedOn\": \"2024-05-05T13:29:16.513Z\",\n" +
                                    "    \"endedOn\": \"2024-05-05T14:29:16.513Z\"\n" +
                                    "  }\n" +
                                    "}"
                    ));
        }

    }

    @Nested
    public class Returns400 {

        @Test
        void should_Return400_When_CreatingSubscriptionAndUserIdIsNull() throws Exception {
            // Given
            ZonedDateTime subscriptionStartDateTime = ZonedDateTime.parse("2024-05-05T16:29:16.513+03:00");

            CreateSubscriptionRequest request = new CreateSubscriptionRequest(null, subscriptionStartDateTime);

            String requestStr = objectMapper.writeValueAsString(request);

            MockHttpServletRequestBuilder requestBuilder = post("/v1/subscriptions")
                    .accept(SUBSCRIPTION)
                    .contentType(CREATE_SUBSCRIPTION)
                    .content(requestStr);

            // When & Then
            validate400(requestBuilder);
        }

        @Test
        void should_Return400_When_CreatingSubscriptionAndSubscriptionStartDateTimeIsNull() throws Exception {
            // Given
            CreateSubscriptionRequest request = new CreateSubscriptionRequest(UUID.randomUUID(), null);

            String requestStr = objectMapper.writeValueAsString(request);

            MockHttpServletRequestBuilder requestBuilder = post("/v1/subscriptions")
                    .accept(SUBSCRIPTION)
                    .contentType(CREATE_SUBSCRIPTION)
                    .content(requestStr);

            // When & Then
            validate400(requestBuilder);
        }

        @Test
        void should_Return400_When_GettingSubscriptionStatusUserIdIsNotUUID() throws Exception {
            // Given
            MockHttpServletRequestBuilder requestBuilder = get("/v1/subscriptions/users/12345")
                    .accept(SUBSCRIPTION_STATUS);

            // When & Then
            validate400(requestBuilder);
        }

        @Test
        void should_Return400_When_ResubscribingUserAndUserIdIsNotUUID() throws Exception {
            // Given
            ZonedDateTime subscriptionStartDateTime = ZonedDateTime.parse("2024-05-05T17:29:16.513+03:00");

            UpdateSubscriptionRequest request = new UpdateSubscriptionRequest(subscriptionStartDateTime, null);

            String requestStr = objectMapper.writeValueAsString(request);

            MockHttpServletRequestBuilder requestBuilder = patch("/v1/subscriptions/users/12345")
                    .accept(SUBSCRIPTION)
                    .contentType(UPDATE_SUBSCRIPTION)
                    .content(requestStr);

            // When & Then
            validate400(requestBuilder);
        }

        @Test
        void should_Return400_When_EndingSubscriptionAndUserIdIsNotUUID() throws Exception {
            // Given
            ZonedDateTime subscriptionEndDateTime = ZonedDateTime.parse("2024-05-05T17:29:16.513+03:00")
                    .withZoneSameInstant(UTC);

            UpdateSubscriptionRequest request = new UpdateSubscriptionRequest(null, subscriptionEndDateTime);

            String requestStr = objectMapper.writeValueAsString(request);

            MockHttpServletRequestBuilder requestBuilder = patch("/v1/subscriptions/users/1234")
                    .accept(SUBSCRIPTION)
                    .contentType(UPDATE_SUBSCRIPTION)
                    .content(requestStr);

            // When & Then
            validate400(requestBuilder);
        }

    }

    @Nested
    public class Returns500 {

        @Test
        void should_Return500_When_CreatingSubscriptionAndCouldntCreateSubscription() throws Exception {
            // Given
            UUID userId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
            ZonedDateTime subscriptionStartDateTime = ZonedDateTime.parse("2024-05-05T16:29:16.513+03:00");

            CreateSubscriptionRequest request = new CreateSubscriptionRequest(userId, subscriptionStartDateTime);

            String requestStr = objectMapper.writeValueAsString(request);

            MockHttpServletRequestBuilder requestBuilder = post("/v1/subscriptions")
                    .accept(SUBSCRIPTION)
                    .contentType(CREATE_SUBSCRIPTION)
                    .content(requestStr);

            given(subscriptionService.createSubscription(userId, subscriptionStartDateTime))
                    .willThrow(RuntimeException.class);

            // When & Then
            validate500(requestBuilder);
        }

        @Test
        void should_Return500_When_GettingSubscriptionStatusAndCouldntRetrieveSubscriptionStatus() throws Exception {
            // Given
            UUID userId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");

            MockHttpServletRequestBuilder requestBuilder = get("/v1/subscriptions/users/" + userId)
                    .accept(SUBSCRIPTION_STATUS);

            given(subscriptionService.isSubscribed(userId)).willThrow(RuntimeException.class);

            // When & Then
            validate500(requestBuilder);
        }

        @Test
        void should_Return500_When_ResubscribingUserAndUpdateSubscriptionStatus() throws Exception {
            // Given
            UUID userId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
            ZonedDateTime subscriptionStartDateTime = ZonedDateTime.parse("2024-05-05T17:29:16.513+03:00");

            UpdateSubscriptionRequest request = new UpdateSubscriptionRequest(subscriptionStartDateTime, null);

            String requestStr = objectMapper.writeValueAsString(request);

            MockHttpServletRequestBuilder requestBuilder = patch("/v1/subscriptions/users/" + userId)
                    .accept(SUBSCRIPTION)
                    .contentType(UPDATE_SUBSCRIPTION)
                    .content(requestStr);

            given(subscriptionService.updateSubscription(userId, subscriptionStartDateTime, null)).willThrow(RuntimeException.class);

            // When & Then
            validate500(requestBuilder);
        }

        @Test
        void should_Return500_When_EndingSubscriptionAndCouldntUpdateUpdateSubscription() throws Exception {
            // Given
            UUID userId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
            ZonedDateTime subscriptionEndDateTime = ZonedDateTime.parse("2024-05-05T17:29:16.513+03:00");

            UpdateSubscriptionRequest request = new UpdateSubscriptionRequest(null, subscriptionEndDateTime);

            String requestStr = objectMapper.writeValueAsString(request);

            MockHttpServletRequestBuilder requestBuilder = patch("/v1/subscriptions/users/" + userId)
                    .accept(SUBSCRIPTION)
                    .contentType(UPDATE_SUBSCRIPTION)
                    .content(requestStr);

            given(subscriptionService.updateSubscription(userId, null, subscriptionEndDateTime)).willThrow(RuntimeException.class);

            // When & Then
            validate500(requestBuilder);
        }

        @Test
        void should_Return500_When_UpdatingSubscriptionAndBothDatesArePresent() throws Exception {
            // Given
            UUID userId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
            ZonedDateTime subscriptionStartDateTime = ZonedDateTime.parse("2024-05-05T16:29:16.513+03:00");
            ZonedDateTime subscriptionEndDateTime = ZonedDateTime.parse("2024-05-05T17:29:16.513+03:00");

            UpdateSubscriptionRequest request = new UpdateSubscriptionRequest(subscriptionStartDateTime, subscriptionEndDateTime);

            String requestStr = objectMapper.writeValueAsString(request);

            MockHttpServletRequestBuilder requestBuilder = patch("/v1/subscriptions/users/" + userId)
                    .accept(SUBSCRIPTION)
                    .contentType(UPDATE_SUBSCRIPTION)
                    .content(requestStr);

            given(subscriptionService.updateSubscription(userId, subscriptionStartDateTime, subscriptionEndDateTime))
                    .willThrow(IllegalArgumentException.class);

            // When & Then
            validate500(requestBuilder);
        }

    }

    private void validate400(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    private void validate500(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        mockMvc.perform(requestBuilder)
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(Matchers.blankString()));
    }

}