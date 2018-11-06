update eh_var_fields set display_name = '楼宇' where id = 10965;
update eh_var_fields set display_name = '房源' where id = 10966;
update eh_var_field_scopes set field_display_name = '楼宇' where field_id = 10965 and field_display_name = '楼栋';
update eh_var_field_scopes set field_display_name = '房源' where field_id = 10966 and field_display_name = '门牌名称';

