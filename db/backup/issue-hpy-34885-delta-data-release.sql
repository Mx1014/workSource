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

--end

-- AUTHOR: 黄鹏宇 20180802
-- REMARK:修复导入导出不能使用的问题
UPDATE `eh_var_fields` SET NAME='dropBox1ItemId' where name = 'dropBox1';
UPDATE `eh_var_fields` SET NAME='dropBox1ItemId' where name = 'dropBox1';
UPDATE `eh_var_fields` SET NAME='dropBox2ItemId' where name = 'dropBox2';
UPDATE `eh_var_fields` SET NAME='dropBox3ItemId' where name = 'dropBox3';
UPDATE `eh_var_fields` SET NAME='dropBox4ItemId' where name = 'dropBox4';
UPDATE `eh_var_fields` SET NAME='dropBox5ItemId' where name = 'dropBox5';
UPDATE `eh_var_fields` SET NAME='dropBox6ItemId' where name = 'dropBox6';
UPDATE `eh_var_fields` SET NAME='dropBox7ItemId' where name = 'dropBox7';
UPDATE `eh_var_fields` SET NAME='dropBox8ItemId' where name = 'dropBox8';
UPDATE `eh_var_fields` SET NAME='dropBox9ItemId' where name = 'dropBox9';

-- END
-- --------------------- SECTION END ---------------------------------------------------------