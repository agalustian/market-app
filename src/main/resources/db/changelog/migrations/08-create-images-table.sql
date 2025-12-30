create table if not exists images
(
    id      integer primary key,
    content bytea not null
)