create table vacancies (
   id serial primary key not null,
   name varchar(2000) not null unique,
   text text,
   link varchar(2000)
);