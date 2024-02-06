INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');

INSERT INTO users (name, email, age, password, username) VALUES ('Alex', 'Alex@gmail', 24, 'user', 'user');
INSERT INTO users (name, email, age, password, username) VALUES ('Boris', 'Boris@gmail', 45, 'admin', 'admin');


INSERT INTO users_roles (users_id, roles_id) VALUES (1, 1);
INSERT INTO users_roles (users_id, roles_id) VALUES (2, 2);

