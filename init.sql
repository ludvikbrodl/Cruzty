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
	orderId			int default NULL,
	cookieName		varchar(55) not NULL,
	isBlocked 		boolean not NULL default 0,
	productionDate	DATETIME default NOW(),
	location		enum('storage', 'customer') not NULL default 'storage',
	deliveryDate	DATETIME default '0000-00-00 00:00:00',
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
	unit 					varchar(55) not NULL,
	storedAmount	double not NULL default 100000,
	primary key 	(ingredientName),
	foreign key		(unit) references UNITS(unit)
);

create table COOKIES_INGREDIENTS(
	cookieName 		varchar(55) not NULL,
	ingredientName	varchar(55) not NULL,
	amount 				double not NULL default 0,
	primary key		(cookieName, ingredientName),
	foreign key		(cookieName) references COOKIES(cookieName),
	foreign key		(ingredientName) references INGREDIENTS(ingredientName)
);

create table ORDERS_COOKIES(
	orderId 			int not NULL,
	cookieName		varchar(55) not NULL,
	nbrPallets 		int not NULL default 0,
	primary key		(orderId, cookieName),
	foreign key		(orderId) references ORDERS(orderId),
	foreign key		(cookieName) references COOKIES(cookieName)
);


insert into COOKIES (cookieName) values ('Nut ring'), ('Nut cookie'), ('Amneris'), ('Tango'), ('Almond delight'), ('Berliner');

insert into UNITS (unit) values ('g'), ('dl');

insert into INGREDIENTS (ingredientName, unit) values
('Flour', 'g'),
('Butter', 'g'),
('Icing sugar', 'g'),
('Roasted, chopped nuts', 'g'),
('Fine-ground nuts', 'g'),
('Bread crumbs', 'g'),
('Sugar', 'g'),
('Egg whites', 'dl'),
('Chocolate', 'g'),
('Potato starch', 'g'),
('Wheat flour', 'g'),
('Sodium bicarbonate', 'g'),
('Vanilla', 'g'),
('Chopped almonds', 'g'),
('Cinnamon', 'g'),
('Marzipan', 'g'),
('Ground, roasted nuts', 'g'),
('Vanilla sugar', 'g'),
('Eggs', 'g');

insert into COOKIES_INGREDIENTS (cookieName, ingredientName, amount) values
('Nut ring', 'Flour', 450),
('Nut ring', 'Butter', 450),
('Nut ring', 'Icing sugar', 190),
('Nut ring', 'Roasted, chopped nuts', 225),

('Nut cookie', 'Fine-ground nuts', 750),
('Nut cookie', 'Ground, roasted nuts', 625),
('Nut cookie', 'Bread crumbs', 125),
('Nut cookie', 'Sugar', 375),
('Nut cookie', 'Egg whites', 3.5),
('Nut ring', 'Chocolate', 50),

('Amneris', 'Marzipan', 750),
('Amneris', 'Butter', 250),
('Amneris', 'Eggs', 250),
('Amneris', 'Potato starch', 25),
('Amneris', 'Wheat flour', 25),

('Tango', 'Butter', 200),
('Tango', 'Sugar', 250),
('Tango', 'Flour', 300),
('Tango', 'Sodium bicarbonate', 4),
('Tango', 'Vanilla', 2),

('Almond delight', 'Butter', 400),
('Almond delight', 'Sugar', 270),
('Almond delight', 'Chopped almonds', 279),
('Almond delight', 'Flour', 400),
('Almond delight', 'Cinnamon', 10),

('Berliner', 'Flour', 350),
('Berliner', 'Butter', 250),
('Berliner', 'Icing sugar', 100),
('Berliner', 'Eggs', 50),
('Berliner', 'Vanilla sugar', 5),
('Berliner', 'Chocolate', 50);


insert into CUSTOMERS (customerName, address) values ('Finkakor AB', 'Helsingborg');

insert into ORDERS (customerName) values ('Finkakor AB');
insert into ORDERS_COOKIES (orderId, cookieName, nbrPallets) VALUES(1, 'Almond delight', 1);
insert into ORDERS_COOKIES (orderId, cookieName, nbrPallets) VALUES(1, 'Berliner', 1);

insert into Pallets (cookieName, orderId) values ('Almond delight', 1);


/*
update INGREDIENTS SET storedAmount = storedAmount -
	(select amount from COOKIES_INGREDIENTS where cookieName = 'Almond delight'
		AND COOKIES_INGREDIENTS.ingredientName = INGREDIENTS.ingredientName)
	where ingredientName in
		(select ingredientName from Cookies natural join COOKIES_INGREDIENTS
 			where cookieName = 'Almond delight');
 		*/
