CREATE TABLE "user"
(
    id        SERIAL8,
    login     VARCHAR(50),
    password  VARCHAR(255),
    full_name VARCHAR(100),
    media_id  BIGINT,
    phone     VARCHAR(11),
    email     VARCHAR(255),
    deleted BOOLEAN DEFAULT FALSE NOT NULL,
    role_id   SERIAL8,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT FK_WORKPLACES_ON_MEDIA FOREIGN KEY (media_id) REFERENCES media (id),
    CONSTRAINT FK_USER_ON_ROLE FOREIGN KEY (role_id) REFERENCES role (id)
);