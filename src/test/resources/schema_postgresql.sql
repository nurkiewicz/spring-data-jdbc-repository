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

CREATE TABLE BOARDING_PASS (
	flight_no VARCHAR(8) NOT NULL,
	seq_no INT NOT NULL,
	passenger VARCHAR(1000),
	seat CHAR(3),
	PRIMARY KEY (flight_no, seq_no)
);