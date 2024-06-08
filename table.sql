-- create database egringotts
-- run above line first before create table, then select the databse name on shemas, on left 
create table admin(
	ID_Admin int,
    Name_Admin varchar(100),
    PhoneNum_Admin varchar(50),
    Email_Admin varchar(100),
    username varchar(20),
    Password_Admin varchar(15),
    DOB date,
    address varchar(100),
    constraint PK_admin primary key(ID_Admin)
);

create table account(
	AccountNum varchar(19),
    Name_Customer varchar(100),
    username varchar(20),
    PhoneNum_Customer varchar(25),
    Email_Customer varchar(100),
    Password_Customer varchar(15),
    DOB date,
    Address varchar(100),
    Tier varchar(50),
    Knut float default 0,
    Sickle float default 0,
    Galleon float default 0,
    constraint PK_account primary key(AccountNum)
);

create table transaction(
	ID_Transaction varchar(10),
    Sender varchar(19),
    Receipent varchar(19),
    Amount varchar(100),
    balance varchar(100),
    method varchar(20),
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

create table card(
	cvv int,
    AccountNum varchar(19),
    Card_Number varchar(19),
    Expiration_Date varchar(5),
    Credit_limit varchar(100),
    type varchar(20),
    constraint PK_card primary key(cvv),
    constraint PF_Holder foreign key (AccountNum)
    references account(AccountNum)
    on delete cascade
);

CREATE TABLE exchange_rate (
    currency_code_from int NOT NULL,
    currency_code_to int NOT NULL,
    rate DECIMAL(9, 6) NOT NULL,
    fee_rate decimal(10,4),
    PRIMARY KEY (currency_code_from, currency_code_to),
    FOREIGN KEY (currency_code_from) REFERENCES currency(code) on delete cascade,
    FOREIGN KEY (currency_code_to) REFERENCES currency(code)  on delete cascade
);

CREATE TABLE currency (
    code int NOT NULL,
    symbol VARCHAR(3) NOT NULL,
    display_name VARCHAR(50) NOT NULL,
    PRIMARY KEY (code)
);
