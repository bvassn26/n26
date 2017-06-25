DROP TABLE transactions IF EXISTS;
CREATE TABLE transactions(id SERIAL, amount DOUBLE, timestamp BIGINT);
CREATE INDEX IDXTIMESTAMP ON transactions(timestamp);