CREATE TABLE user_in_chat
(
    id      BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_id BIGINT,
    chat_id BIGINT,
    CONSTRAINT pk_user_in_chat PRIMARY KEY (id)
);

ALTER TABLE user_in_chat
    ADD CONSTRAINT FK_USER_IN_CHAT_ON_CHAT FOREIGN KEY (chat_id) REFERENCES chat (id);

ALTER TABLE user_in_chat
    ADD CONSTRAINT FK_USER_IN_CHAT_ON_USER FOREIGN KEY (user_id) REFERENCES workflow."user" (id);