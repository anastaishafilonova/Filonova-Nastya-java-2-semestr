create table outbox (
    id BIGINT primary key,
    data text not null
);