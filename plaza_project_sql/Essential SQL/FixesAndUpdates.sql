USE `storemarketing`;
-- call add_store(1,'Toha Heavy Industries', '1 Sidonia Docks', '85d4541h4', 2555, 40000, 'Engineering', 1, 'Sidonia Docks', 1, 'Sidonia', 1, 'Gravitational Beam Emitter', 40000, null, 99999999.99, null);
-- call add_store(2, 'Kunato Development', 'Industrial Sector', '7844541d', '2736', '40000', '', '1', '', '1', '', '2', 'Type 22 Garde', 40000, '', 99999999.99, null);
UPDATE StoreTbl s SET s.yearOpened = 1999 WHERE s.yearOpened IS NULL;
UPDATE StoreTbl s SET s.ownerID = 1 WHERE s.ownerID IS NULL;
UPDATE StoreTbl s SET s.typeID = 1 WHERE s.typeID IS NULL;
UPDATE MerchandiseTbl m SET m.merchPic = 'sample.jpg';
UPDATE MerchandiseTbl m SET m.merchType = 1 WHERE m.merchType IS NULL;
UPDATE MerchandiseTbl m SET m.merchName = TRIM(m.merchName);