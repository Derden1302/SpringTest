CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table public.customer (
    id uuid primary key DEFAULT uuid_generate_v4(),
    name varchar(10)  NOT NULL
);

create table public.account (
    id uuid primary key DEFAULT uuid_generate_v4(),
    customer_id uuid UNIQUE,
    account_data varchar(100)  NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES public.customer (id) ON DELETE CASCADE
);

create table public.seller(
    id uuid primary key DEFAULT uuid_generate_v4(),
    name varchar(10)  NOT NULL
);

create table public.item (
    id uuid primary key DEFAULT uuid_generate_v4(),
    name varchar(10)  NOT NULL,
    seller_id uuid REFERENCES seller(id) on DELETE CASCADE
);

create table public.contract(
    id uuid primary key DEFAULT uuid_generate_v4(),
    name varchar(10)  NOT NULL
);

create table public.history(
    id uuid primary key DEFAULT uuid_generate_v4(),
    name varchar(10)  NOT NULL
);

create table public.contract_history (
    contract_id uuid REFERENCES contract(id) ON DELETE CASCADE,
    history_id uuid REFERENCES history(id) ON DELETE CASCADE,
    PRIMARY KEY (contract_id, history_id)
);