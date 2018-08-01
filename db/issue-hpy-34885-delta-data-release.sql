-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 黄鹏宇 20180801
-- REMARK:20180731新增字段插入数据修改
-- 购买/租赁
set @eecid=(select id from `eh_var_fields` where name='buyOrLease');
update `eh_var_field_items` set field_id = @eecid where field_id = @eecid-1 and display_name in ('租赁','购买');
update `eh_var_fields` set name = 'buyOrLeaseItemId' where id = @eecid;

set @eecid=(select id from `eh_var_fields` where name='financingDemand');
update `eh_var_field_items` set field_id = @eecid where field_id = @eecid-1 and display_name in ('有','无');
update `eh_var_fields` set name = 'financingDemandItemId' where id = @eecid;

UPDATE `eh_var_fields` set field_type = 'Long' where name in ('buyOrLeaseItemId','financingDemandItemId',
'dropBox1','dropBox2','dropBox3','dropBox4','dropBox5','dropBox6','dropBox7','dropBox8','dropBox9');

-- END
-- --------------------- SECTION END ---------------------------------------------------------