-- 停车订单标签 by dengs,2018.04.27
update eh_parking_lots SET order_tag=SUBSTR(id FROM 3 FOR 5);