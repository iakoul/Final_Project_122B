#for whatever reason, you have to run the first line manually the first time or
#mysql won't find the database... even though the first line should only execute
#if the database doesn't exist. It works on Windows but not Linux, and only if
#ran from this script.

mysql -uroot -pmysqlpass storemarketing < tina_plaza_table.sql
mysql -uroot -pmysqlpass storemarketing < queries_all.sql
mysql -uroot -pmysqlpass storemarketing < AddStoreStoredProcedurePrototype.sql
mysql -uroot -pmysqlpass storemarketing < 10000_10000_10.xml.sql
mysql -uroot -pmysqlpass storemarketing < FixesAndUpdates.sql
mysql -uroot -pmysqlpass storemarketing < FindAndAddItemsToStores.sql