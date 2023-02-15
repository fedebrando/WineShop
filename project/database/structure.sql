DROP DATABASE IF EXISTS WineShop;
CREATE DATABASE WineShop;
USE WineShop;

CREATE TABLE User
(
	Username VARCHAR(15) NOT NULL CHECK(Username <> ''),
	PswHash CHAR(128) NOT NULL,
	Type VARCHAR(13) NOT NULL CHECK(Type in ('Customer', 'Employee', 'Administrator')),
	Name VARCHAR(30) NOT NULL,
	Surname VARCHAR(30) NOT NULL,
	FiscalCode CHAR(16) DEFAULT NULL,
	Telephone VARCHAR(15) DEFAULT NULL,
	Address VARCHAR(30) DEFAULT NULL,
	PRIMARY KEY (Username)
);

CREATE TABLE Wine
(
	ID INT auto_increment NOT NULL,
	Name VARCHAR(30) NOT NULL,
	Producer VARCHAR(30) NOT NULL,
	Origin VARCHAR(30) DEFAULT NULL,
	Year INT(4) NOT NULL CHECK(Year > 1800),
	Notes VARCHAR(100) DEFAULT NULL,
	Vines VARCHAR(100) DEFAULT NULL,
	Price FLOAT(7, 2) NOT NULL CHECK(Price > 0),
	AvailableQuantity INT NOT NULL DEFAULT 1 CHECK(AvailableQuantity >= 0),
	PRIMARY KEY (ID)
);

CREATE TABLE Service
(
	FiscalCode CHAR(16) NOT NULL,
	Type VARCHAR(8) NOT NULL CHECK(Type in ('Supplier', 'Courier')),
	Name VARCHAR(30) NOT NULL,
	Surname VARCHAR(30) NOT NULL,
	Telephone VARCHAR(15) DEFAULT NULL,
	CompanyAddress VARCHAR(30) DEFAULT NULL,
	PRIMARY KEY (FiscalCode)
);

CREATE TABLE ShoppingCart
(
	Customer VARCHAR(15) NOT NULL,
	IDWine INT NOT NULL,
	Quantity INT NOT NULL DEFAULT 1 CHECK(Quantity >= 0),
	PRIMARY KEY (Customer, IDWine),
	FOREIGN KEY (Customer) REFERENCES User(Username) ON DELETE CASCADE,
	FOREIGN KEY (IDWine) REFERENCES Wine(ID) ON DELETE CASCADE
);

CREATE TABLE Purchase
(
	ID INT auto_increment NOT NULL,
	User VARCHAR(15) NOT NULL,
	DigitalSignature VARCHAR(15) DEFAULT NULL,
	IDWine INT NOT NULL,
	FCService CHAR(16) DEFAULT NULL,
	Date DATETIME NOT NULL,
	Proposal BIT NOT NULL,
	Quantity INT NOT NULL DEFAULT 1 CHECK(Quantity >= 0),
	Price FLOAT(7, 2) DEFAULT NULL CHECK(Price > 0),
	DeliveryDate DATE DEFAULT NULL CHECK(DeliveryDate >= date(Date)),
	PRIMARY KEY (ID),
	FOREIGN KEY (User) REFERENCES User(Username) ON DELETE CASCADE,
	FOREIGN KEY (DigitalSignature) REFERENCES User(Username) ON DELETE CASCADE,
	FOREIGN KEY (IDWine) REFERENCES Wine(ID) ON DELETE CASCADE,
	FOREIGN KEY (FCService) REFERENCES Service(FiscalCode) ON DELETE CASCADE
);