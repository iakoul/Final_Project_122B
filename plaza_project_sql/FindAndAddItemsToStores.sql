SELECT 
    m.merchID
FROM
    MerchandiseTbl m
WHERE
    m.merchID NOT IN (SELECT 
            s.merchID
        FROM
            StoreSellsTbl s);
            
INSERT INTO StoreSellsTbl VALUES(6260010001001, 72011);
INSERT INTO StoreSellsTbl VALUES(6260010001001, 72055);
INSERT INTO StoreSellsTbl VALUES(6260010001001, 72059);
INSERT INTO StoreSellsTbl VALUES(6260010001001, 72003);
INSERT INTO StoreSellsTbl VALUES(6260010001001, 8767);
INSERT INTO StoreSellsTbl VALUES(6260010001001, 8505);

SELECT 
    m.merchID
FROM
    MerchandiseTbl m
WHERE
    m.merchID NOT IN (SELECT 
            s.merchID
        FROM
            StoreSellsTbl s);