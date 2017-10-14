-- added by R for form
ALTER TABLE `eh_general_forms` ADD COLUMN `modify_flag` TINYINT DEFAULT 1 COMMENT 'whether the form can be modified from desk, 0: no, 1: yes';
ALTER TABLE `eh_general_forms` ADD COLUMN `delete_flag` TINYINT DEFAULT 1 COMMENT 'whether the form can be deleted from desk, 0: no, 1: yes';

ALTER TABLE `eh_general_approvals` ADD COLUMN `modify_flag` TINYINT DEFAULT 1 COMMENT 'whether the approval can be modified from desk, 0: no, 1: yes';
ALTER TABLE `eh_general_approvals` ADD COLUMN `delete_flag` TINYINT DEFAULT 1 COMMENT 'whether the approval can be deleted from desk, 0: no, 1: yes';
ALTER TABLE `eh_general_approvals` ADD COLUMN `icon_uri` VARCHAR(1024) COMMENT "the avatar of the approval";
ALTER TABLE `eh_general_approvals` ADD COLUMN `approval_type` VARCHAR(64) DEFAULT 'CUSTOMIZE' COMMENT "the type of the approval";