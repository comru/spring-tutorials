create table article (
       id bigint not null,
        created_at timestamp(6) with time zone not null,
        updated_at timestamp(6) with time zone not null,
        body varchar(255) not null,
        description varchar(255) not null,
        slug varchar(255) not null,
        title varchar(255) not null,
        author_id bigint not null,
        primary key (id)
    );

    create table article_favouring_users (
       article_id bigint not null,
        favouring_users_id bigint not null,
        primary key (article_id, favouring_users_id)
    );

    create table article_tags (
       article_id bigint not null,
        tags_id bigint not null,
        primary key (article_id, tags_id)
    );

    create table comment (
       id bigint not null,
        created_at timestamp(6) with time zone not null,
        updated_at timestamp(6) with time zone not null,
        body varchar(255) not null,
        author_id bigint not null,
        article_id bigint,
        primary key (id)
    );

    create table tag (
       id bigint not null,
        name varchar(255) not null,
        primary key (id)
    );

    create table user_ (
       id bigint not null,
        bio text,
        email varchar(255) not null,
        image text,
        password_hash varchar(255) not null,
        username varchar(255) not null,
        primary key (id)
    );

    create table users_follow (
       follower_id bigint not null,
        following_id bigint not null,
        primary key (follower_id, following_id)
    );


    alter table if exists user_
       add constraint UK_ha67cvlhy4nk1prswl5gj1y0y unique (email);

    alter table if exists user_
       add constraint UK_wqsqlvajcne4rlyosglqglhk unique (username);
 create sequence article_seq start with 1 increment by 50;
 create sequence comment_seq start with 1 increment by 50;
 create sequence tag_seq start with 1 increment by 50;
 create sequence user__seq start with 1 increment by 50;


    alter table if exists article
       add constraint FKnpb9tnlonvcsrhql5eg4le9cj
       foreign key (author_id)
       references user_;

    alter table if exists article_favouring_users
       add constraint FKlxm6fnsy864149b7x1q19a6xn
       foreign key (favouring_users_id)
       references user_;

    alter table if exists article_favouring_users
       add constraint FKdplhynfhcirnoc9b886ipoc87
       foreign key (article_id)
       references article;

    alter table if exists article_tags
       add constraint FKp6owh2p5p9yllwwrc2hn7bnxr
       foreign key (tags_id)
       references tag;

    alter table if exists article_tags
       add constraint FK85ph188kqbfc5u1gq0tme7flk
       foreign key (article_id)
       references article;

    alter table if exists comment
       add constraint FKeygnqcngb7dixfa8lngm5h4f5
       foreign key (author_id)
       references user_;

    alter table if exists comment
       add constraint FK5yx0uphgjc6ik6hb82kkw501y
       foreign key (article_id)
       references article;

    alter table if exists users_follow
       add constraint FK4cmrp5kbpdg9s0e793hryoccn
       foreign key (following_id)
       references user_;

    alter table if exists users_follow
       add constraint FKpsqbavat08rdi5gnaxov4f9fi
       foreign key (follower_id)
       references user_;