CREATE DATABASE IF NOT EXISTS `storemarketing` DEFAULT CHARACTER SET latin1;
USE `storemarketing`;

DROP TABLE IF EXISTS `ItemsSoldTbl`;
DROP TABLE IF EXISTS `StoreSellsTbl`;
DROP TABLE IF EXISTS `MerchandiseTypeTbl`;
DROP TABLE IF EXISTS `MerchandiseTbl`;
DROP TABLE IF EXISTS `CreditCardsTbl`;
DROP TABLE IF EXISTS `UsersTbl`;
DROP TABLE IF EXISTS `AcceptsPaymentTbl`;
DROP TABLE IF EXISTS `StoreTbl`;
DROP TABLE IF EXISTS `PlazaTbl`;
DROP TABLE IF EXISTS `CityTbl`;
DROP TABLE IF EXISTS `OwnerTbl`;
DROP TABLE IF EXISTS `LanguageTbl`;
DROP TABLE IF EXISTS `StoreTypeTbl`;

CREATE TABLE `StoreTypeTbl` (    
    `typeID` INTEGER NOT NULL AUTO_INCREMENT,
    `storeType` CHAR(100) NOT NULL,
    PRIMARY KEY (`typeID`)
) AUTO_INCREMENT=0 ENGINE=INNODB DEFAULT CHARSET=LATIN1;

CREATE TABLE `OwnerTbl` (
    `ownerID` INTEGER NOT NULL AUTO_INCREMENT,
    `name` CHAR(50) NOT NULL,
    `phoneNum` CHAR(20) NOT NULL,
    PRIMARY KEY (`ownerID`)
) AUTO_INCREMENT=0 ENGINE=INNODB DEFAULT CHARSET=LATIN1;

CREATE TABLE `CityTbl` (
    `cityID` INTEGER NOT NULL AUTO_INCREMENT,
    `cityName` CHAR(100) NOT NULL,
    PRIMARY KEY (`cityID`)
) AUTO_INCREMENT=0 ENGINE=INNODB DEFAULT CHARSET=LATIN1;

CREATE TABLE `PlazaTbl` (
    `plazaID` BIGINT NOT NULL AUTO_INCREMENT,
    `plazaName` CHAR(100) NOT NULL,
    `cityID` INTEGER NOT NULL,
    PRIMARY KEY (`plazaID`),
    FOREIGN KEY (`cityID`)
        REFERENCES `CityTbl` (`cityID`)
        ON DELETE CASCADE ON UPDATE CASCADE
) AUTO_INCREMENT=0 ENGINE=INNODB DEFAULT CHARSET=LATIN1;

CREATE TABLE `StoreTbl` (
    `storeID` BIGINT NOT NULL AUTO_INCREMENT,
    `storeName` CHAR(100) NOT NULL,
    `address` CHAR(100) NOT NULL,
    `phoneNum` CHAR(20),
    `yearOpened` INTEGER,
	`typeID` INTEGER,
    `plazaID` BIGINT,
    `ownerID` INTEGER DEFAULT 0,
    PRIMARY KEY (`storeID`),
    FOREIGN KEY (`typeID`) 
        REFERENCES `StoreTypeTbl` (`typeID`)
        ON DELETE CASCADE ON UPDATE CASCADE,  
    FOREIGN KEY (`plazaID`)
        REFERENCES `PlazaTbl` (`plazaID`)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`ownerID`)
        REFERENCES `OwnerTbl` (`ownerID`)
        ON DELETE CASCADE ON UPDATE CASCADE
) AUTO_INCREMENT=0 ENGINE=INNODB DEFAULT CHARSET=LATIN1;

CREATE TABLE `AcceptsPaymentTbl` (
    `storeID` BIGINT NOT NULL,
    `acceptsVisa` BOOLEAN NOT NULL,
	`acceptsMasterCard` BOOLEAN NOT NULL,
	`acceptsDiscover` BOOLEAN NOT NULL,
	`acceptsAmEx` BOOLEAN NOT NULL,
	`acceptsPaypal` BOOLEAN NOT NULL,
	`acceptsVenmo` BOOLEAN NOT NULL,
    PRIMARY KEY (`storeID`),
    FOREIGN KEY (`storeID`)
        REFERENCES `StoreTbl` (`storeID`)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=LATIN1;

CREATE TABLE `UsersTbl` (
	`username` CHAR(20) NOT NULL,
	`firstName` CHAR(100) NOT NULL DEFAULT '',
	`lastName` CHAR(100) NOT NULL DEFAULT '',
    `hashPW` CHAR(60) NOT NULL,
    `isAdmin` BOOL NOT NULL DEFAULT 0,
    PRIMARY KEY (`username`)
) ENGINE=INNODB DEFAULT CHARSET=LATIN1;

CREATE TABLE `CreditCardsTbl` (
	`CCID` CHAR(20) NOT NULL,
	`firstName` CHAR(50) NOT NULL DEFAULT '',
	`lastName` CHAR(50) NOT NULL DEFAULT '',
	`expiration` DATE NOT NULL,
	PRIMARY KEY (`CCID`)
) ENGINE=INNODB DEFAULT CHARSET=LATIN1;

CREATE TABLE `MerchandiseTbl` (
	`merchID` INTEGER NOT NULL AUTO_INCREMENT,
	`merchName` CHAR(100) NOT NULL DEFAULT '',
	`merchType` INTEGER DEFAULT 35,
	`merchPrice` DECIMAL(10,2) NOT NULL,
	`merchPic` VARCHAR(100) DEFAULT '',
	PRIMARY KEY (`merchID`),
	FOREIGN KEY (`merchType`)
		REFERENCES `StoreTypeTbl` (`typeID`)
		ON DELETE CASCADE ON UPDATE CASCADE
) AUTO_INCREMENT=0 ENGINE=INNODB DEFAULT CHARSET=LATIN1;

CREATE TABLE `MerchandiseTypeTbl` (
    `merchTypeID` INTEGER NOT NULL AUTO_INCREMENT,
    `merchTypeName` CHAR(100) NOT NULL DEFAULT '',
    PRIMARY KEY (`merchTypeID`)
) AUTO_INCREMENT=0 ENGINE=INNODB DEFAULT CHARSET=LATIN1;


CREATE TABLE `StoreSellsTbl` (
	`storeID` BIGINT NOT NULL,
	`merchID` INTEGER NOT NULL,
	PRIMARY KEY (`storeID`, `merchID`),
	    FOREIGN KEY (`storeID`)
        REFERENCES `StoreTbl` (`storeID`)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`merchID`)
        REFERENCES `MerchandiseTbl` (`merchID`)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=LATIN1;

CREATE TABLE `ItemsSoldTbl` (
	`username` CHAR(20) NOT NULL,
	`dateSold` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	`CCID` CHAR(20) NOT NULL,
	`merchID` INTEGER NOT NULL,
	`storeID` BIGINT NOT NULL,
	`qty` INTEGER NOT NULL,
	PRIMARY KEY (`username`, `dateSold`, `CCID`, `merchID`, `storeID`, `qty`),
	FOREIGN KEY (`username`)
		REFERENCES `UsersTbl` (`username`),
	FOREIGN KEY (`CCID`)
		REFERENCES `CreditCardsTbl` (`CCID`),
	FOREIGN KEY (`merchID`)
		REFERENCES `MerchandiseTbl` (`merchID`),
	FOREIGN KEY (`storeID`)
		REFERENCES `StoreTbl` (`storeID`)
) ENGINE=INNODB DEFAULT CHARSET=LATIN1;
