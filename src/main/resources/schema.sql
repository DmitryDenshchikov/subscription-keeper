CREATE TABLE IF NOT EXISTS subscription (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE,
    started_on TIMESTAMP NOT NULL,
    ended_on TIMESTAMP
);
