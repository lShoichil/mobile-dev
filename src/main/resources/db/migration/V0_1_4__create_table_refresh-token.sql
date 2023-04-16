CREATE TABLE refresh_token
(
    id        SERIAL8,
    user_id   SERIAL8,
    CONSTRAINT pk_refreshToken PRIMARY KEY (id),
    CONSTRAINT FK_REFRESH_TOKEN_ON_USER FOREIGN KEY (user_id) REFERENCES "user" (id)
);