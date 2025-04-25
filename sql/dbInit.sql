CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL
);

CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL,
    description VARCHAR(200)
);

CREATE TABLE user_roles (
    user_id BIGINT,
    role_id BIGINT,
    FOREIGN KEY(user_id) REFERENCES users(id),
    FOREIGN KEY(role_id) REFERENCES roles(id)
);

CREATE TABLE permissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    permission_name VARCHAR(100) NOT NULL,
    description VARCHAR(200)
);

CREATE TABLE role_permissions (
    role_id BIGINT,
    permission_id BIGINT,
    FOREIGN KEY(role_id) REFERENCES roles(id),
    FOREIGN KEY(permission_id) REFERENCES permissions(id)
);


