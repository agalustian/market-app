create table if not exists orders
(
    id          serial primary key,
    order_items jsonb     not null,
    total_sum   integer   not null,
    created_at  timestamp not null default now()
);