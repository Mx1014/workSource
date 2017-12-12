
CREATE TABLE `eh_relocation_requests` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` varchar(32) DEFAULT NULL COMMENT 'attachment object owner type',
  `owner_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'owner id',
  `request_no` varchar(128) NOT NULL,
  `requestor_enterprise_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the id of organization where the requestor is in',
  `requestor_enterprise_name` varchar(64) DEFAULT NULL COMMENT 'the enterprise name of requestor',
  `requestor_enterprise_address` varchar(256) DEFAULT NULL COMMENT 'the enterprise address of requestor',
  `requestor_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'requestor id',
  `requestor_name` varchar(64) DEFAULT NULL COMMENT 'the name of requestor',
  `contact_phone` varchar(64) DEFAULT NULL COMMENT 'the phone of requestor',
  `relocation_date` datetime NOT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '0: inactive, 1: processing, 2: completed',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `flow_case_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'flow case id',
  `cancel_time` datetime DEFAULT NULL,
  `cancel_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'cancel user id',
  `qr_code_url` varchar(256) DEFAULT NULL COMMENT 'url of the qr record',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_relocation_request_items` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` varchar(32) DEFAULT NULL COMMENT 'attachment object owner type',
  `owner_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'owner id',
  `request_id` bigint(20) NOT NULL COMMENT 'id of the relocation request record',
  `item_name` varchar(64) DEFAULT NULL COMMENT 'the name of item',
  `item_quantity` int(11) DEFAULT 0 COMMENT 'the quantity of item',
  `status` tinyint(4) DEFAULT NULL COMMENT '0: inactive, 1: , 2: active',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_relocation_request_attachments` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_type` varchar(32) DEFAULT NULL COMMENT 'attachment object owner type',
  `owner_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'owner id',
  `content_type` varchar(32) DEFAULT NULL COMMENT 'attachment object content type',
  `content_uri` varchar(1024) DEFAULT NULL COMMENT 'attachment object link info on storage',
  `status` tinyint(4) DEFAULT NULL COMMENT '0: inactive, 1: , 2: active',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


ALTER TABLE eh_enterprise_op_requests ADD COLUMN `category_id` bigint(20) DEFAULT NULL;
ALTER TABLE eh_lease_promotions ADD COLUMN `category_id` bigint(20) DEFAULT NULL;
ALTER TABLE eh_lease_projects ADD COLUMN `category_id` bigint(20) DEFAULT NULL;
ALTER TABLE eh_lease_project_communities ADD COLUMN `category_id` bigint(20) DEFAULT NULL;
ALTER TABLE eh_lease_issuers ADD COLUMN `category_id` bigint(20) DEFAULT NULL;
ALTER TABLE eh_lease_form_requests ADD COLUMN `category_id` bigint(20) DEFAULT NULL;
ALTER TABLE eh_lease_configs ADD COLUMN `category_id` bigint(20) DEFAULT NULL;
ALTER TABLE eh_lease_buildings ADD COLUMN `category_id` bigint(20) DEFAULT NULL;
