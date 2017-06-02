# replace nopasses with your own password :)

mysql -uroot -pnopasses storemarketing < tina_plaza_table.sql
mysql -uroot -pnopasses storemarketing < queries_all.sql
mysql -uroot -pnopasses storemarketing < AddStoreStoredProcedurePrototype.sql
mysql -uroot -pnopasses storemarketing < 10000_10000_10.sql
mysql -uroot -pnopasses storemarketing < FixesAndUpdates.sql