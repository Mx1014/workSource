-- 薪酬2.2 begin
-- added by wh :薪酬工资条发放消息
SET @template_id = (SELECT MAX(id) FROM eh_locale_templates);
INSERT INTO `eh_locale_templates`(`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@template_id := @template_id + 1, 'salary.notification', 1, 'zh_CN', '后台发工资条', '${salaryDate} 工资已发放。', 0);

-- added by wh 薪酬的模块对应action_type 改为74(工资条)
UPDATE eh_service_modules SET action_type = 74 WHERE id = 51400;
UPDATE eh_service_module_apps SET action_type = 74 WHERE module_id = 51400;
-- 薪酬2.2 end


-- 客户管理第三方数据跟进人  by jiarui
UPDATE  eh_enterprise_customers set tracking_uid = NULL  WHERE  tracking_uid = -1;
update eh_enterprise_customer_admins  set namespace_id  = (select namespace_id from eh_enterprise_customers where id = customer_id);

update eh_enterprise_customer_admins t2  set contact_type  = (select target_type from eh_organization_members t1 where t1.contact_token = t2.contact_token and t1.namespace_id = t2.namespace_id limit 1);


