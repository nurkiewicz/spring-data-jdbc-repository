-- Schema can be used for MS SQLServer 2008 and 2012

IF OBJECT_ID('USERS', 'U') IS NOT NULL
  DROP TABLE USERS;
GO
CREATE TABLE USERS (
  user_name     VARCHAR(255) PRIMARY KEY,
  date_of_birth DATE NOT NULL, --timestamp columns can not be used for explicit inserts
  reputation    INT  NOT NULL,
  enabled       BIT  NOT NULL
);

IF OBJECT_ID('COMMENTS', 'U') IS NOT NULL
  DROP TABLE COMMENTS;
GO
CREATE TABLE COMMENTS (
  id              INT IDENTITY (1, 1) PRIMARY KEY,
  user_name       VARCHAR(256),
  contents        VARCHAR(1000),
  created_time    DATETIME NOT NULL, --timestamp columns can not be used for explicit inserts
  favourite_count INT      NOT NULL
);


IF OBJECT_ID('BOARDING_PASS', 'U') IS NOT NULL
  DROP TABLE BOARDING_PASS;
GO
CREATE TABLE BOARDING_PASS (
  flight_no VARCHAR(8) NOT NULL,
  seq_no    INT        NOT NULL,
  passenger VARCHAR(1000),
  seat      CHAR(3)
    CONSTRAINT PK_BOARDING_PASS PRIMARY KEY CLUSTERED (flight_no, seq_no)
);
