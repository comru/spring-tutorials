create table owner
(
    owner_id   bigint auto_increment primary key,
    owner_name varchar(256) not null,
    address    varchar(500) not null
);

create table dog
(
    dog_id     bigint auto_increment primary key,
    name       varchar(200) not null,
    owner_id varchar(256)
);

alter table dog
    add foreign key (owner_id) references owner (owner_id);