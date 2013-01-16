CREATE SEQUENCE user_seq;

CREATE TABLE USERS (
	user_name varchar(255) PRIMARY KEY DEFAULT nextval('user_seq'),
	date_of_birth DATE NOT NULL,
	reputation INT NOT NULL,
	enabled BOOLEAN NOT NULL
);

CREATE SEQUENCE comment_seq;

CREATE TABLE IF NOT EXISTS COMMENTS (
  id INT PRIMARY KEY DEFAULT nextval('comment_seq'),
  user_name varchar(256) REFERENCES USERS,
  contents varchar(1000),
  created_time TIMESTAMP NOT NULL,
  favourite_count INT NOT NULL
);