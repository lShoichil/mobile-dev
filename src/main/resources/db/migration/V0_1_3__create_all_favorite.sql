CREATE TABLE favorite
(
    id               SERIAL8,
    user_id          SERIAL8,
    favorite_user_id SERIAL8,
    CONSTRAINT pk_favorite PRIMARY KEY (id),
    CONSTRAINT FK_FAVORITE_ON_FAVORITE_USER FOREI GN KEY (favorite_user_id) REFERENCES "user" (id),
    CONSTRAINT FK_FAVORITE_ON_USER FOREIGN KEY (user_id) REFERENCES "user" (id)
);