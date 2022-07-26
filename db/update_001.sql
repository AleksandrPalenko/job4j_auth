create table if not exists person(
    id       serial primary key not null,
    login    varchar(2000),
    password varchar(2000),
    employee_id integer
);

insert into person (login, password, employee_id) values ('parsentev', '123', 1);
insert into person (login, password, employee_id) values ('parsentev2', '123', 1);
insert into person (login, password, employee_id) values ('ban', '123', 3);

create table if not exists employees(
    id  serial primary key not null,
    name varchar(2000),
    inn varchar(2000),
    created timestamp
);

create table if not exists person_employees(
    id serial primary key,
    person_id int not null references person(id),
    employees_id int not null references employees(id)
);

insert into employees (name, inn, created) values ('User1', 963258741, '2022-07-26 09:50:13.000000');
insert into employees (name, inn, created) values ('User2', 123456789, '2022-07-26 09:55:13.000000');
insert into employees (name, inn, created) values ('User3', 258963147, '2022-07-26 09:59:13.000000');
