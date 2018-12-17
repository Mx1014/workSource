ALTER TABLE eh_flow_nodes ADD COLUMN form_relation_type TINYINT DEFAULT '0' NOT NULL COMMENT '1: 关联自定义表单, 2: 关联eh_flows关联的表单';
ALTER TABLE eh_flow_nodes ADD COLUMN form_relation_data TEXT COMMENT '表单关联数据，json格式';


ALTER TABLE eh_flows ADD COLUMN form_status TINYINT DEFAULT '0' NOT NULL COMMENT '0: disable, 1: enable';
ALTER TABLE eh_flows ADD COLUMN form_relation_type TINYINT DEFAULT '0' NOT NULL COMMENT '1: 关联自定义表单, 2: 关联eh_flows关联的表单';
ALTER TABLE eh_flows ADD COLUMN form_relation_data TEXT COMMENT '表单关联数据，json格式';
