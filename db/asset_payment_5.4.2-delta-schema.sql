ALTER TABLE `eh_payment_bills` ADD COLUMN `payment_type` int DEFAULT null COMMENT '账单的支付方式（0-线下缴费，1-微信支付，2-对公转账，8-支付宝支付）';

ALTER TABLE `eh_payment_bills` ADD COLUMN `certificate_note` varchar(255) DEFAULT NULL COMMENT '上传凭证图片时附加的留言';

ALTER TABLE `eh_payment_bills` ADD COLUMN `is_upload_certificate` tinyint(4) DEFAULT null COMMENT '该账单是否上传了缴费凭证（0:否，1：是）';