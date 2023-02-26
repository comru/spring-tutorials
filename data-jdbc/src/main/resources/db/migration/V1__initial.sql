create table owner
(
    owner_id   bigint auto_increment primary key,
    owner_name varchar(256) not null,
    country    varchar(500),
    city       varchar(500)
);

create table dog
(
    dog_id     bigint auto_increment primary key,
    name       varchar(200) not null,
    owner_id   bigint
);

alter table dog
    add foreign key (owner_id) references owner (owner_id);