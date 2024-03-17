create sequence if not exists bets_seq start with 1 increment by 50;
create sequence if not exists players_seq start with 1 increment by 50;
create sequence if not exists rounds_seq start with 1 increment by 50;

create table if not exists bets (
    guessed_number integer not null,
    is_won boolean default false,
    stake numeric(7, 2),
    bet_dt timestamp(6) not null,
    id bigint not null,
    player_id bigint,
    round_id bigint,
    primary key (id)
);

create table if not exists players (
    balance numeric(7, 2),
    id bigint not null,
    nickname varchar(255) not null unique,
    primary key (id)
);

create table if not exists rounds (
    is_finished boolean default false,
    number integer,
    id bigint not null,
    start_ts timestamp(6),
    primary key (id)
);