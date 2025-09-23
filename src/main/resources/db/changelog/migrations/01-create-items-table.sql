create table if not exists items
(
    id          serial primary key,
    title       text      not null,
    description text      not null,
    price       int       not null,
    created_at  timestamp not null default now(),
    updated_at  timestamp
);