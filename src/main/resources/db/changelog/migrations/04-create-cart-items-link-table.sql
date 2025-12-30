create table if not exists cart_items
(
    id      serial primary key,
    cart_id integer references carts (id),
    item_id integer references items (id),
    count   integer not null
);