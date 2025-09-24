create table if not exists cart_items
(
    id         serial primary key,
    cart_id    integer references items (id),
    item_id    integer references items (id),
    count      integer   not null,
    created_at timestamp not null default now(),
    updated_at timestamp
);