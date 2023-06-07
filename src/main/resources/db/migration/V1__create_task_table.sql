CREATE TABLE task (
  id UUID PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  done BOOLEAN NOT NULL,
  created TIMESTAMP NOT NULL,
  priority VARCHAR(20)
);