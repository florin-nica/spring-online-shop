drop table if exists users;
drop table if exists authorities;
drop table if exists users_authorities;
create table users
(
  id       integer generated by default as identity,
  username varchar_ignorecase(255) not null unique,
  password varchar(255)            not null,
  enabled  boolean                 not null,
  primary key (id)
);

create table authorities
(
  id   integer generated by default as identity,
  name varchar(255) not null,
  primary key (id)
);

create table users_authorities
(
  user_id      integer not null,
  authority_id integer not null,
  primary key (user_id, authority_id)
);

alter table users_authorities
  add constraint FK_user_id foreign key (user_id) references users (id);
alter table users_authorities
  add constraint FK_authority_id foreign key (authority_id) references authorities (id);

INSERT INTO USERS (id, username, password, enabled)
VALUES (1, 'user1', '$2a$10$K4iGtwBsYEk33WWHHiWVyOZjsRJdDy/DQRhKNKZ4/aYs2/jYa8a52', true),
       (2, 'user2', '$2a$10$dUSpjCw3nKdEsDxWGo1CReIks7HhrJm1hZiX8mcIvq9U397S3Q3/y', false);

INSERT INTO AUTHORITIES (id, name)
VALUES (1, 'CUSTOMER');

INSERT INTO USERS_AUTHORITIES (user_id, authority_id)
VALUES (1, 1),
       (2, 1);