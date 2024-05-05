package denshchikov.dmitry.app.constant;

import lombok.Getter;

@Getter
public final class MediaType {

    public static final String CREATING_SUBSCRIPTION = "application/vnd.prs.v1.creating-subscription+json";
    public static final String ENDING_SUBSCRIPTION = "application/vnd.prs.v1.ending-subscription+json";
    public static final String REACTIVATING_SUBSCRIPTION = "application/vnd.prs.v1.reactivating-subscription+json";

    public static final String CREATED_SUBSCRIPTION = "application/vnd.prs.v1.created-subscription+json";
    public static final String SUBSCRIPTION_STATUS = "application/vnd.prs.v1.subscription-status+json";
    public static final String ENDED_SUBSCRIPTION = "application/vnd.prs.v1.ended-subscription+json";
    public static final String REACTIVATED_SUBSCRIPTION = "application/vnd.prs.v1.reactivated-subscription+json";

    private MediaType() {
        throw new UnsupportedOperationException("This class is dedicated to media type constants");
    }

}

