create table if not exists images
(
    id      integer primary key references items (id),
    content bytea not null
)