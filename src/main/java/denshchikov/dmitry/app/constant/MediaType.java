package denshchikov.dmitry.app.constant;

public final class MediaType {

    public static final String CREATE_SUBSCRIPTION = "application/vnd.prs.v1.create-subscription+json";
    public static final String UPDATE_SUBSCRIPTION = "application/vnd.prs.v1.update-subscription+json";

    public static final String SUBSCRIPTION = "application/vnd.prs.v1.subscription+json";
    public static final String SUBSCRIPTION_STATUS = "application/vnd.prs.v1.subscription-status+json";

    private MediaType() {
        throw new UnsupportedOperationException("This class is dedicated to media type constants");
    }

}

