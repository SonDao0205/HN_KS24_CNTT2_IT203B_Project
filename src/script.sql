DROP DATABASE IF EXISTS Project;
CREATE DATABASE IF NOT EXISTS Project;
USE Project;

CREATE TABLE Customers( -- user
    id int primary key auto_increment,
    username varchar(255) not null unique ,
    fullName varchar(255) not null ,
    password varchar(255) not null ,
    role ENUM('customer','chef','manager'),
    status BOOLEAN default true
);

CREATE TABLE Tables( -- bàn
    id int primary key auto_increment,
    number varchar(255) not null unique ,
    capacity int not null ,
    status ENUM('available','occupied','reserved','block') default 'available'
);

CREATE TABLE Categories( -- danh mục
    id int primary key auto_increment,
    name VARCHAR(255) not null,
    status boolean default true
);

create table Menu_Items( -- món trong thực đơn
    id int primary key auto_increment,
    categories_id int,
    foreign key (categories_id) references Categories(id),
    name varchar(255) not null ,
    price decimal(15,2) not null check ( price > 0 ),
    status BOOLEAN default true
);


create table Orders( -- phiếu đặt hàng
    id int primary key auto_increment,
    customer_id int,
    table_id int,
    foreign key (customer_id) references Customers(id),
    foreign key (table_id) references Tables(id),
    total_amount decimal(15,2) not null check ( total_amount >= 0 ),
    status enum('PENDING','PAID','CANCEL') default 'PENDING',
    created_at datetime default current_timestamp
);

create table Order_Items( -- chi tiết sản phẩm trong phiếu đặt hàng
    id int primary key auto_increment,
    order_id int,
    menu_item int,
    unit_price decimal(15,2) not null check ( unit_price > 0 ),
    quantity int not null check ( quantity > 0 ),
    status enum('pending','cooking','ready','served','cancel') default 'pending',
    note varchar(255)
);