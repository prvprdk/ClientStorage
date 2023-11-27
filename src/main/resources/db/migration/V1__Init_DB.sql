create sequence access_seq start with 1 increment by 50;
create sequence client_seq start with 1 increment by 50;
create sequence site_seq start with 1 increment by 50;
create sequence usr_seq start with 1 increment by 50;
create table access
    (type_accesses smallint check (type_accesses between 0 and 3),
     id bigint not null,
     site_id bigint,
     login varchar(255),
     password varchar(255),
     url varchar(255),
     primary key (id));

create table client
    (contract smallint check (contract between 0 and 2),
    id bigint not null, company varchar(255),
    email varchar(255),
    name varchar(255),
    number varchar(255),
    site varchar(255),
    primary key (id));

create table  site (
    id bigint not null,
    name varchar(255),
    primary key (id));

create table  usr (
    id bigint not null,
    password varchar(255),
    username varchar(255) not null,
    primary key (id));

alter table if exists access add constraint FK5xa96780pvqfedpkpee0frnyf foreign key (site_id) references site;
