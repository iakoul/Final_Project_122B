CREATE DATABASE IF NOT EXISTS `StoreMarketing` DEFAULT CHARACTER SET latin1;
USE `StoreMarketing`;

DROP TABLE IF EXISTS `UsersTbl`;
DROP TABLE IF EXISTS `AcceptsPaymentTbl`;
DROP TABLE IF EXISTS `StoreTbl`;
DROP TABLE IF EXISTS `PlazaTbl`;
DROP TABLE IF EXISTS `CityTbl`;
DROP TABLE IF EXISTS `OwnerTbl`;
DROP TABLE IF EXISTS `LanguageTbl`;
DROP TABLE IF EXISTS `AcceptedPaymentsTbl`;
DROP TABLE IF EXISTS `StoreTypeTbl`;

CREATE TABLE `StoreTypeTbl` (    
    `typeID` INTEGER NOT NULL AUTO_INCREMENT,
    `storeType` CHAR(20) NOT NULL,
    PRIMARY KEY (`typeID`)
) AUTO_INCREMENT=0 ENGINE=INNODB DEFAULT CHARSET=LATIN1;

CREATE TABLE `AcceptedPaymentsTbl` (
    `paymentID` INTEGER NOT NULL AUTO_INCREMENT,
    `paymentType` CHAR(20) NOT NULL,
    PRIMARY KEY (`paymentID`)
) AUTO_INCREMENT=0 ENGINE=INNODB DEFAULT CHARSET=LATIN1;

CREATE TABLE `LanguageTbl` (
    `langID` INTEGER NOT NULL AUTO_INCREMENT,
    `languageSpoken` CHAR(10) NOT NULL,
    PRIMARY KEY (`langID`)
) AUTO_INCREMENT=0 ENGINE=INNODB DEFAULT CHARSET=LATIN1;

CREATE TABLE `OwnerTbl` (
    `ownerID` INTEGER NOT NULL,
    `name` CHAR(50) NOT NULL,
    `phoneNum` CHAR(20) NOT NULL,
    PRIMARY KEY (`ownerID`)
) ENGINE=INNODB DEFAULT CHARSET=LATIN1;

CREATE TABLE `CityTbl` (
    `cityID` INTEGER NOT NULL AUTO_INCREMENT,
    `cityName` CHAR(100) NOT NULL,
    PRIMARY KEY (`cityID`)
) AUTO_INCREMENT=0 ENGINE=INNODB DEFAULT CHARSET=LATIN1;

CREATE TABLE `PlazaTbl` (
    `plazaID` DOUBLE NOT NULL AUTO_INCREMENT,
    `plazaName` CHAR(100) NOT NULL,
    `cityID` INTEGER NOT NULL,
    PRIMARY KEY (`plazaID`),
    FOREIGN KEY (`cityID`)
        REFERENCES `CityTbl` (`cityID`)
        ON DELETE CASCADE ON UPDATE CASCADE
) AUTO_INCREMENT=0 ENGINE=INNODB DEFAULT CHARSET=LATIN1;

CREATE TABLE `StoreTbl` (
    `storeID` DOUBLE NOT NULL AUTO_INCREMENT,
    `plazaName` CHAR(100) NOT NULL,
    `address` CHAR(100) NOT NULL,
    `phoneNum` CHAR(20),
    `yearOpened` INTEGER,
	`typeID` INTEGER,
    `plazaID` DOUBLE,
    `ownerID` INTEGER,
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
    `storeID` DOUBLE NOT NULL,
    `paymentID` INTEGER NOT NULL,
    PRIMARY KEY (`storeID`, `paymentID`),
    FOREIGN KEY (`storeID`)
        REFERENCES `StoreTbl` (`storeID`)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`paymentID`)
        REFERENCES `AcceptedPaymentsTbl` (`paymentID`)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=LATIN1;

CREATE TABLE `UsersTbl` (
	`username` CHAR(20) NOT NULL,
    `hashPW` CHAR(60) NOT NULL,
    `isAdmin` BOOL NOT NULL DEFAULT 0,
    PRIMARY KEY (`username`)
) ENGINE=INNODB DEFAULT CHARSET=LATIN1;