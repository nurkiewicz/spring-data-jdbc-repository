DROP SEQUENCE comment_seq;
DROP TABLE COMMENTS;
DROP TABLE users;
DROP TABLE BOARDING_PASS;


CREATE TABLE USERS (
  user_name     VARCHAR(255),
  date_of_birth DATE NOT NULL,
  reputation    INT  NOT NULL,
  enabled       INT  NOT NULL,
  CONSTRAINT pk_users_user_name PRIMARY KEY (user_name)
);


CREATE SEQUENCE comment_seq
START WITH 1000
INCREMENT BY 1
NOCACHE
;

CREATE TABLE COMMENTS (
  id              INT,
  user_name       VARCHAR(256) REFERENCES USERS,
  contents        VARCHAR(1000),
  created_time    TIMESTAMP NOT NULL,
  favourite_count INT       NOT NULL,
  CONSTRAINT pk_comment_id PRIMARY KEY (id)
);

CREATE OR REPLACE TRIGGER COMMENT_ID_GEN
BEFORE INSERT ON COMMENTS
FOR EACH ROW
  BEGIN
    SELECT
      comment_seq.nextval
    INTO :new.id
    FROM dual;
  END;
/


CREATE TABLE BOARDING_PASS (
  flight_no VARCHAR(8) NOT NULL,
  seq_no    INT        NOT NULL,
  passenger VARCHAR(1000),
  seat      CHAR(3),
  CONSTRAINT pk_BOARDING_PASS_fn_sn PRIMARY KEY (flight_no, seq_no)
);