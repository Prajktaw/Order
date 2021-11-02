drop database if exists order_db;
create database order_db;
use order_db;
create table order_table(
order_id varchar(30) primary key,
buyer_id varchar(30),
amount float,
date Date,
address varchar(50),
status varchar(20))
;

insert into order_table
values('108','101',20.3,'2021-11-11','adressakla','good');

create table products_ordered(
buyer_id varchar(30),
product_id varchar(30),
seller_id varchar(30),
quantity integer,
constraint products_ordered_pk primary key (product_id,buyer_id)
 );
 
 insert into products_ordered
 values('102','103','104',7);
 
 select * from order_table;
 select * from products_ordered;