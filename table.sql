-- create database egringotts
-- run above line first before create table, then select the databse name on shemas, on left 
create table admin(
	ID_Admin int,
    Name_Admin varchar(100),
    PhoneNum_Admin varchar(25),
    Email_Admin varchar(100),
    Password_Admin varchar(15),
    constraint PK_admin primary key(ID_Admin)
);

create table account(
	AccountNum varchar(19),
    username varchar(50),
    PhoneNum_Customer varchar(25),
    Email_Customer varchar(100),
    Password_Customer varchar(15),
    DOB date,
    Address varchar(100),
    Balance int,
    Tier varchar(50),
    constraint PK_account primary key(AccountNum)
);

create table transaction(
	ID_Transaction varchar(10),
    Sender varchar(19),
    Receipent varchar(19),
    Amount int,
    balance int,
    Type varchar(25),
    Date datetime default now(),
    Description varchar(100),
    constraint PK_transaction primary key(ID_Transaction),
    constraint PF_MakeTrans foreign key (Sender)
    references account(AccountNum)
    on delete cascade,
    constraint PF_Receiver foreign key (Receipent)
    references account(AccountNum)
    on delete cascade
);

