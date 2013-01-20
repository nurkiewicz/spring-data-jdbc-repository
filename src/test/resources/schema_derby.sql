CREATE TABLE USERS (
	user_name varchar(256) NOT NULL PRIMARY KEY,
	date_of_birth TIMESTAMP NOT NULL,
	reputation INT NOT NULL,
	enabled BOOLEAN NOT NULL
);

CREATE TABLE COMMENTS (
	id INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	user_name varchar(256),
	contents varchar(1000),
	created_time TIMESTAMP NOT NULL,
	favourite_count INT NOT NULL,
	FOREIGN KEY (user_name) REFERENCES USERS(user_name)
);

CREATE TABLE BOARDING_PASS (
  flight_no varchar(8) NOT NULL,
  seq_no INT NOT NULL,
  passenger VARCHAR(1000),
  seat CHAR(3),
  PRIMARY KEY (flight_no, seq_no)
);
