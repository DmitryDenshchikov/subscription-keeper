package denshchikov.dmitry.app.constant;

public final class MediaType {

    public static final String CREATING_SUBSCRIPTION = "application/vnd.prs.v1.creating-subscription+json";
    public static final String ENDING_SUBSCRIPTION = "application/vnd.prs.v1.ending-subscription+json";
    public static final String REACTIVATING_SUBSCRIPTION = "application/vnd.prs.v1.reactivating-subscription+json";

    public static final String SUBSCRIPTION = "application/vnd.prs.v1.subscription+json";
    public static final String SUBSCRIPTION_STATUS = "application/vnd.prs.v1.subscription-status+json";

    private MediaType() {
        throw new UnsupportedOperationException("This class is dedicated to media type constants");
    }

}

