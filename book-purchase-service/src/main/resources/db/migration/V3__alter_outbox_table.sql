alter table outbox drop column id;
alter table outbox add column id BIGSERIAL primary key;