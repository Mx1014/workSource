-- 增加联系人职位 add by xiongying 20170914
ALTER TABLE eh_enterprise_customers ADD COLUMN contact_position VARCHAR(64);
-- 增加域空间资源排序 add by lvlei 20170915
ALTER TABLE eh_namespace_resources ADD COLUMN default_order int(11) DEFAULT 0;