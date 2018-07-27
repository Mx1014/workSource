-- 通用脚本
-- AUHOR: jiarui 20180726
-- REMARK：动态表单迁移ownerId
update eh_var_field_group_scopes t1 set owner_id = (select id  from eh_organizations  t2 where organization_type = 'PM' and t2.namespace_id = t1.namespace_id and parent_id = 0 LIMIT 1 );
update eh_var_field_group_scopes set owner_type ='EhOrganizations';
update eh_var_field_scopes t1 set owner_id = (select id  from eh_organizations  t2 where organization_type = 'PM' and t2.namespace_id = t1.namespace_id and parent_id = 0 LIMIT 1 );
update eh_var_field_scopes set owner_type ='EhOrganizations';
update eh_var_field_item_scopes t1 set owner_id = (select id  from eh_organizations  t2 where organization_type = 'PM' and t2.namespace_id = t1.namespace_id and parent_id = 0 LIMIT 1 );
update eh_var_field_item_scopes set owner_type ='EhOrganizations';
-- 物业巡检
update eh_equipment_inspection_equipments set owner_type = 'EhOrganizations';
update eh_equipment_inspection_standards set owner_type = 'EhOrganizations';
update eh_equipment_inspection_accessories set owner_type = 'EhOrganizations';
update eh_equipment_inspection_plans set owner_type = 'EhOrganizations';
update eh_equipment_inspection_tasks set owner_type = 'EhOrganizations';
update eh_equipment_inspection_templates set owner_type = 'EhOrganizations';
update eh_pm_notify_configurations t1 set target_id = (select t2.id  from eh_organizations  t2 where t2.organization_type = 'PM' and t2.namespace_id =(select namespace_id from eh_communities t3 where t3.id = scope_id ) and t2.parent_id = 0 LIMIT 1 ) where t1.scope_type = 2;
update eh_pm_notify_configurations t1 set target_id = (select t2.id  from eh_organizations  t2 where t2.organization_type = 'PM' and t2.namespace_id =t1.scope_id and t2.parent_id = 0 LIMIT 1 ) where t1.scope_type = 1;
update eh_equipment_inspection_review_date t1 set target_id = (select t2.id  from eh_organizations  t2 where t2.organization_type = 'PM' and t2.namespace_id =(select namespace_id from eh_communities t3 where t3.id = scope_id ) and t2.parent_id = 0 LIMIT 1 ) where t1.scope_type = 2;
update eh_equipment_inspection_review_date t1 set target_id = (select t2.id  from eh_organizations  t2 where t2.organization_type = 'PM' and t2.namespace_id =t1.scope_id and t2.parent_id = 0 LIMIT 1 ) where t1.scope_type = 1;



-- 企业客户
update eh_enterprise_customers set owner_type ='EhOrganizations';