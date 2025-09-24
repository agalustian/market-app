create table if not exists carts
(
    id         serial primary key,
    item_id    integer references items (id),
    count      integer   not null,
    created_at timestamp not null default now()
);