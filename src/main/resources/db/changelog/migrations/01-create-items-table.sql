create table if not exists items
(
    id          serial primary key,
    title       text not null,
    description text not null,
    price       int  not null
);