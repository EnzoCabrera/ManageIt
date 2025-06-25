create table users (
    id bigint primary key unique not null,
    email varchar(100) not null unique,
    password varchar(255) not null,
    role varchar(50) not null,
    created_at timestamp with time zone default now()
);