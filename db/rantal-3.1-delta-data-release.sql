-- 初始化为按时长定价 by st.zheng
update `eh_rentalv2_price_rules` set `price_type` = 0;
update `eh_rentalv2_price_packages` set `price_type` = 0;
update `eh_rentalv2_cells` set `price_type` = 0;