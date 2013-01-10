CREATE SEQUENCE user_seq;

CREATE TABLE USER (
	user_name varchar(255) PRIMARY KEY DEFAULT nextval(user_seq),
	date_of_birth TIMESTAMP NOT NULL,
	reputation INT NOT NULL,
	enabled BIT(1) NOT NULL
);

CREATE SEQUENCE comment_seq;

CREATE TABLE IF NOT EXISTS COMMENT (
  id INT PRIMARY KEY PRIMARY KEY DEFAULT nextval(comment_seq),
  user_name varchar(256),
  contents varchar(1000),
  created_time TIMESTAMP NOT NULL,
  favourite_count INT NOT NULL
);