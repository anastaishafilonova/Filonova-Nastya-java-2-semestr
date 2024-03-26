CREATE TABLE books
(
    id     BIGSERIAL PRIMARY KEY,
    author_id   BIGINT REFERENCES authors (id) ON DELETE CASCADE,
    title  TEXT NOT NULL
);