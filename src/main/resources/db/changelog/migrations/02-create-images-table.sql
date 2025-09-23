create table if not exists images
(
    id         serial primary key,
    item_id    integer  references items (id),
    img_path   text      not null,
    created_at timestamp not null default now(),
    updated_at timestamp
);