create table if not exists order_items
(
    id       integer primary key,
    order_id integer,
    title    text    not null,
    price    integer not null,
    count    integer not null
)
