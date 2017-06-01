-- looks up how many stores sell each merchandise (you can see switch it to be stores sell how many items)
select merchname as `merch name`, merchtbl.merchid, count(merchtbl.merchid) from MerchandiseTbl merchtbl, storesellstbl sellstbl
	where merchtbl.merchid = sellstbl.merchid
    group by merchid;

-- ordered by stores now
select storename as `store name`, sellstbl.storeid, count(storetbl.storeid) as occurances from StoreTbl storetbl, StoreSellsTbl sellstbl
	where sellstbl.storeid = StoreTbl.storeid
    group by storename, storetbl.storeid
    order by occurances desc;


-- looks up stuff that are actually linked to stores
SELECT merchtbl.merchid, merchtbl.merchname
    FROM MerchandiseTbl merchtbl
    INNER JOIN storesellstbl sellstbl ON sellstbl.merchid = merchtbl.merchid
    group by merchtbl.merchid;
