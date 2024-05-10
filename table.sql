-- create database egringotts
-- run above line first before create table, then select the databse name on shemas, on left 
create table admin(
	ID_Admin int,
    Name_Admin varchar(100),
    PhoneNum_Admin varchar(100),
    Email_Admin varchar(100),
    Password_Admin varchar(15),
    constraint PK_admin primary key(ID_Admin)
);

create table customer(
	ID_Customer int,
    Name_Customer varchar(100),
    PhoneNum_Customer varchar(100),
    Email_Customer varchar(100),
    Password_Customer varchar(15),
    DOB date,
    Address varchar(100),
    constraint PK_customer primary key(ID_Customer)
);

create table account(
	AccountNum varchar(19),
    ID_Customer int,
    Balance int,
    Tier varchar(50),
    constraint PK_account primary key(AccountNum),
    constraint PF_CustAccount foreign key (ID_Customer)
    references customer(ID_Customer)
    on delete cascade
);

create table transaction(
	ID_Transaction int,
    AccountNum varchar(19),
    Amount int,
    Type varchar(25),
    Date datetime default now(),
    Description varchar(100),
    constraint PK_transaction primary key(ID_Transaction),
    constraint PF_MakeTrans foreign key (AccountNum)
    references account(AccountNum)
    on delete cascade
);

