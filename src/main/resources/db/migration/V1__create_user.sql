CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
create table public.users (
    id uuid primary key DEFAULT uuid_generate_v4(),
    username varchar(100)  NOT NULL
);
