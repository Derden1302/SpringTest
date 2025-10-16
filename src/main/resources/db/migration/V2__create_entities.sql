CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table public.customers (
    id uuid primary key DEFAULT uuid_generate_v4(),
    name varchar(10)  NOT NULL
);

create table public.accounts (
    id uuid primary key DEFAULT uuid_generate_v4(),
    customer_id uuid UNIQUE,
    account_data varchar(100)  NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES public.customers (id) ON DELETE CASCADE
);

create table public.sellers(
    id uuid primary key DEFAULT uuid_generate_v4(),
    name varchar(10)  NOT NULL
);

create table public.items (
    id uuid primary key DEFAULT uuid_generate_v4(),
    name varchar(10)  NOT NULL,
    seller_id uuid REFERENCES sellers(id) on DELETE CASCADE
);

create table public.contracts(
    id uuid primary key DEFAULT uuid_generate_v4(),
    name varchar(10)  NOT NULL
);

create table public.history(
    id uuid primary key DEFAULT uuid_generate_v4(),
    name varchar(10)  NOT NULL
);

create table public.contract_history (
    contract_id uuid REFERENCES contracts(id) ON DELETE CASCADE,
    history_id uuid REFERENCES history(id) ON DELETE CASCADE,
    event_date timestamp NOT NULL,
    PRIMARY KEY (contract_id, history_id)
);