ALTER TABLE `eh_customer_commercials` ADD COLUMN `main_business` VARCHAR(256) COMMENT '主营业务';
ALTER TABLE `eh_customer_commercials` ADD COLUMN `branch_company_name` VARCHAR(256) COMMENT '分公司名称';
ALTER TABLE `eh_customer_commercials` ADD COLUMN `branch_registered_date` DATETIME COMMENT '分公司登记日期';
ALTER TABLE `eh_customer_commercials` ADD COLUMN `legal_representative_name` VARCHAR(256) COMMENT '法人代表名称';
ALTER TABLE `eh_customer_commercials` ADD COLUMN `legal_representative_contact` VARCHAR(256) COMMENT '法人联系方式';
ALTER TABLE `eh_customer_commercials` ADD COLUMN `shareholder_name` VARCHAR(256) COMMENT '股东姓名';
ALTER TABLE `eh_customer_commercials` ADD COLUMN `actual_capital_injection_situation` VARCHAR(256) COMMENT '实际注资情况';
ALTER TABLE `eh_customer_commercials` ADD COLUMN `shareholding_situation` VARCHAR(256) COMMENT '股权占比情况';




