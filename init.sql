set foreign_key_checks = 0;
drop table if exists CUSTOMERS, ORDERS, PALLETS, COOKIES, INGREDIENTS, COOKIES_INGREDIENTS, ORDERS_COOKIES, UNITS;
set foreign_key_checks = 1;

create table CUSTOMERS(
	customerName 	varchar(55) not NULL,
	address 		varchar(55) not NULL,
	primary key 	(customerName)
);

create table ORDERS(
	orderId 		int AUTO_INCREMENT,
	customerName 	varchar(55) not NULL,
	deliveryDate	DATETIME default '0000-00-00 00:00:00',
	primary key		(orderId),
	foreign key		(customerName) references CUSTOMERS(customerName)
);

create table COOKIES(
	cookieName		varchar(55) not NULL,
	primary key		(cookieName)
);

create table PALLETS(
	palletId		int AUTO_INCREMENT,
	orderId			int default -1,
	cookieName		varchar(55) not NULL,
	isBlocked 		boolean not NULL default 0,
	productionDate	DATETIME default NOW(),
	location		enum('storage', 'customer') not NULL default 'storage',
	primary key		(palletId),
	foreign key		(orderId) references ORDERS(orderId),
	foreign key		(cookieName) references COOKIES(cookieName)
);

create table UNITS(
	unit 			varchar(55) not NULL,
	primary key		(unit)
);

create table INGREDIENTS(
	ingredientName	varchar(55) not NULL,
	unit 			varchar(55) not NULL,
	storedAmount	int not NULL default 0,
	primary key 	(ingredientName),
	foreign key		(unit) references UNITS(unit)
);

create table COOKIES_INGREDIENTS(
	cookieName 		varchar(55) not NULL,
	ingredientName	varchar(55) not NULL,
	amount 			int not NULL default 0,
	primary key		(cookieName, ingredientName),
	foreign key		(cookieName) references COOKIES(cookieName),
	foreign key		(ingredientName) references INGREDIENTS(ingredientName)
);

create table ORDERS_COOKIES(
	orderId 		int not NULL,
	cookieName		varchar(55) not NULL,
	nbrPallets 		int not NULL default 0,
	primary key		(orderId, cookieName),
	foreign key		(orderId) references ORDERS(orderId),
	foreign key		(cookieName) references COOKIES(cookieName)
);


insert into COOKIES (cookieName) values ('Nut ring'), ('Nut cookie'), ('Amneris'), ('Tango'), ('Almond delight'), ('Berliner');

