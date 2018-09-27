-- 通用脚本
-- AUHOR: jiarui 20180726
-- REMARK：动态表单迁移ownerId
update eh_var_field_group_scopes t1 set owner_id = IFNULL((select id  from eh_organizations  t2 where organization_type = 'PM' and t2.namespace_id = t1.namespace_id and parent_id = 0 LIMIT 1 ),0);
update eh_var_field_group_scopes set owner_type ='EhOrganizations';
update eh_var_field_scopes t1 set owner_id = IFNULL((select id  from eh_organizations  t2 where organization_type = 'PM' and t2.namespace_id = t1.namespace_id and parent_id = 0 LIMIT 1 ),0);
update eh_var_field_scopes set owner_type ='EhOrganizations';
update eh_var_field_item_scopes t1 set owner_id = IFNULL((select id  from eh_organizations  t2 where organization_type = 'PM' and t2.namespace_id = t1.namespace_id and parent_id = 0 LIMIT 1),0);
update eh_var_field_item_scopes set owner_type ='EhOrganizations';
-- 物业巡检 by jiatui 20180730
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
update eh_equipment_inspection_review_date set target_type = 'EhOrganizations';
update eh_pm_notify_configurations set target_type = 'EhOrganizations';
-- 品质核查 by jiarui  20180730
update eh_quality_inspection_standards set owner_type ='EhOrganizations';
update eh_quality_inspection_tasks set owner_type ='EhOrganizations';
update eh_quality_inspection_task_templates set owner_type ='EhOrganizations';
update eh_quality_inspection_specifications set owner_type ='EhOrganizations';
update eh_quality_inspection_samples set owner_type ='EhOrganizations';
update eh_quality_inspection_sample_score_stat set owner_type ='EhOrganizations';
update eh_quality_inspection_sample_community_specification_stat set owner_type ='EhOrganizations';
update eh_quality_inspection_logs set owner_type ='EhOrganizations';
update eh_quality_inspection_evaluations set owner_type ='EhOrganizations';
-- 能耗管理  by jiarui 20180731
update eh_energy_meter_categories set owner_type ='EhOrganizations';

-- 合同管理 by jiarui 20180731
update eh_contract_templates t1 set org_id = (select t2.id  from eh_organizations  t2 where t2.organization_type = 'PM' and t2.namespace_id =t1.namespace_id and t2.parent_id = 0 LIMIT 1);
update eh_contract_params t1 set owner_id = (select id  from eh_organizations  t2 where organization_type = 'PM' and t2.namespace_id = t1.namespace_id and parent_id = 0 LIMIT 1 );
update eh_contract_params set owner_type = 'EhOrganizations';


-- 缴费管理  by jiarui 20180806
UPDATE eh_asset_bills set owner_type ='EhOrganizations';
UPDATE eh_asset_bill_template_fields set owner_type ='EhOrganizations';
UPDATE  eh_payment_charging_item_scopes t1 set org_id = (select t2.id  from eh_organizations  t2 where t2.organization_type = 'PM' and t2.namespace_id =t1.namespace_id and t2.parent_id = 0 LIMIT 1);
update eh_asset_bill_notify_records set owner_type = 'EhOrganizations';