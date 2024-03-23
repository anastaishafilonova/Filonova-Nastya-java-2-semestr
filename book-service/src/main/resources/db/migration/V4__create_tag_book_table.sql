CREATE TABLE tag_book
(
    tag_id   BIGINT REFERENCES tags (id)   NOT NULL,
    book_id BIGINT REFERENCES books (id) NOT NULL,
    PRIMARY KEY (book_id, tag_id)
);