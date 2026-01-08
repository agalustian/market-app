create table if not exists items
(
    id          serial primary key,
    title       text not null,
    img_path    text not null,
    description text not null,
    price       int  not null
);

create table if not exists cart_items
(
    id      serial primary key,
    user_id varchar(50) not null,
    item_id integer references items (id),
    count   integer not null
);

create table if not exists orders
(
    id         serial primary key,
    total_sum  integer   not null,
    user_id    varchar(50)   not null,
    created_at timestamp not null default now()
);

create table if not exists images
(
    item_id integer primary key,
    content bytea not null
);

create table if not exists order_items
(
    id       integer primary key,
    order_id integer,
    title    text    not null,
    price    integer not null,
    count    integer not null
);
