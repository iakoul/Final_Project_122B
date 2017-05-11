USE `storemarketing`;
DROP PROCEDURE IF EXISTS add_store;
DELIMITER //
CREATE PROCEDURE add_store(IN sID BIGINT, IN sName CHAR(100), IN sAddr CHAR(100), IN sPhone CHAR(20), IN sYear INTEGER, IN sTypeID INTEGER, IN sType CHAR(100), IN sPlazaID BIGINT, IN sPlazaName CHAR(100), IN sCityID INTEGER,
	IN sCityName CHAR(100), IN mIDNum INTEGER, IN mName CHAR(100), IN mTypeID INTEGER, IN mType CHAR(100), IN mPrice DECIMAL(10,2), IN mPic VARCHAR(100))
BEGIN

	IF NOT EXISTS (SELECT * FROM StoreTbl s WHERE s.storeID = sID) THEN
		IF NOT EXISTS (SELECT * FROM StoreTypeTbl st WHERE st.typeID = sTypeID) THEN
			INSERT INTO StoreTypeTbl VALUES (sTypeID, sType);
		END IF ;
		IF NOT EXISTS (SELECT * FROM PlazaTbl p WHERE p.plazaID = sPlazaID) THEN
			IF NOT EXISTS (SELECT * FROM CityTbl c WHERE c.cityID = sCityID) THEN
				INSERT INTO CityTbl VALUES (sCityID, sCityName);
			END IF ;
			INSERT INTO PlazaTbl VALUES (sPlazaID, sPlazaName, sCityID);
		END IF ;
		INSERT INTO StoreTbl VALUES (sID, sName, sAddr, sPhone, sYear, sTypeID, sPlazaID, 1);
		IF NOT EXISTS (SELECT * FROM MerchandiseTbl m WHERE m.merchID = mIDNum) THEN
			IF NOT EXISTS (SELECT * FROM StoreTypeTbl st WHERE st.typeID = mTypeID) THEN
				INSERT INTO StoreTypeTbl VALUES (mTypeID, mType);
			END IF ;
			INSERT INTO MerchandiseTbl VALUES (mIDNum, mName, mTypeID, mPrice, mPic);
		END IF ;
		IF NOT EXISTS (SELECT * FROM StoreSellsTbl s WHERE s.merchID = mIDNum AND s.storeID = sID) THEN
			INSERT INTO StoreSellsTbl VALUES (sID, mIDNum);
		END IF ;
	END IF ;
END
//
DELIMITER ;