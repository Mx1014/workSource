-- rental3.6 by st.zheng
INSERT INTO `eh_locale_strings` ( `scope`, `code`, `locale`, `text`) VALUES ( 'rental.flow', 'scene', 'zh_CN', '用户类型');

update `eh_rentalv2_cells` set `cell_id` = `id` where `cell_id`is null;

UPDATE eh_rentalv2_resource_orders
SET begin_time = resource_rental_date,end_time = date_add(resource_rental_date,INTERVAL 1 MONTH) WHERE rental_type = 4 AND begin_time IS NULL AND end_time IS NULL AND resource_rental_date IS NOT NULL;
UPDATE eh_rentalv2_cells
SET begin_time = resource_rental_date,end_time = date_add(resource_rental_date,INTERVAL 1 MONTH) WHERE rental_type = 4 AND begin_time IS NULL AND end_time IS NULL AND resource_rental_date IS NOT NULL;

UPDATE eh_rentalv2_resource_orders
SET begin_time = resource_rental_date,end_time = date_add(resource_rental_date,INTERVAL 1 week) WHERE rental_type = 5 AND begin_time IS NULL AND end_time IS NULL AND resource_rental_date IS NOT NULL;
UPDATE eh_rentalv2_cells
SET begin_time = resource_rental_date,end_time = date_add(resource_rental_date,INTERVAL 1 week) WHERE rental_type = 5 AND begin_time IS NULL AND end_time IS NULL AND resource_rental_date IS NOT NULL;

UPDATE eh_rentalv2_resource_orders
SET begin_time = resource_rental_date,end_time = date_add(resource_rental_date,INTERVAL 1 day) WHERE rental_type = 2 AND begin_time IS NULL AND end_time IS NULL AND resource_rental_date IS NOT NULL;
UPDATE eh_rentalv2_cells
SET begin_time = resource_rental_date,end_time = date_add(resource_rental_date,INTERVAL 1 day) WHERE rental_type = 2 AND begin_time IS NULL AND end_time IS NULL AND resource_rental_date IS NOT NULL;

UPDATE eh_rentalv2_resource_orders
SET begin_time = date_add(resource_rental_date,INTERVAL 8 hour),end_time = date_add(resource_rental_date,INTERVAL 12 hour) WHERE (rental_type = 1 or rental_type = 3) and amorpm = 0 AND begin_time IS NULL AND end_time IS NULL AND resource_rental_date IS NOT NULL;
UPDATE eh_rentalv2_cells
SET begin_time = date_add(resource_rental_date,INTERVAL 8 hour),end_time = date_add(resource_rental_date,INTERVAL 12 hour) WHERE (rental_type = 1 or rental_type = 3) and amorpm = 0 AND begin_time IS NULL AND end_time IS NULL AND resource_rental_date IS NOT NULL;

UPDATE eh_rentalv2_resource_orders
SET begin_time = date_add(resource_rental_date,INTERVAL 14 hour),end_time = date_add(resource_rental_date,INTERVAL 18 hour) WHERE (rental_type = 1 or rental_type = 3) and amorpm = 1 AND begin_time IS NULL AND end_time IS NULL AND resource_rental_date IS NOT NULL;
UPDATE eh_rentalv2_cells
SET begin_time = date_add(resource_rental_date,INTERVAL 14 hour),end_time = date_add(resource_rental_date,INTERVAL 18 hour) WHERE (rental_type = 1 or rental_type = 3) and amorpm = 1 AND begin_time IS NULL AND end_time IS NULL AND resource_rental_date IS NOT NULL;

UPDATE eh_rentalv2_resource_orders
SET begin_time = date_add(resource_rental_date,INTERVAL 18 hour),end_time = date_add(resource_rental_date,INTERVAL 22 hour) WHERE  rental_type = 3 and amorpm = 2 AND begin_time IS NULL AND end_time IS NULL AND resource_rental_date IS NOT NULL;
UPDATE eh_rentalv2_cells
SET begin_time = date_add(resource_rental_date,INTERVAL 18 hour),end_time = date_add(resource_rental_date,INTERVAL 22 hour) WHERE  rental_type = 3 and amorpm = 2 AND begin_time IS NULL AND end_time IS NULL AND resource_rental_date IS NOT NULL;