CREATE TABLE IF NOT EXISTS "user" (
    id UUID PRIMARY KEY,
    created_on TIMESTAMP NOT NULL,
    updated_on TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS subscription (
    id UUID PRIMARY KEY,
    user_id UUID,
    started_on TIMESTAMP NOT NULL,
    ended_on TIMESTAMP,
    created_on TIMESTAMP NOT NULL,
    updated_on TIMESTAMP NOT NULL,

    FOREIGN KEY (user_id) REFERENCES "user"(id)
);
