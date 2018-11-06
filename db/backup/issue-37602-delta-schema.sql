-- AUTHOR: 张智伟 20180914
-- REMARK: ISSUE-37602 表单关联工作流节点
ALTER TABLE eh_general_approval_vals ADD COLUMN flow_node_id BIGINT COMMENT '表单绑定的工作流节点ID' AFTER flow_case_id;
ALTER TABLE eh_general_approval_vals ADD COLUMN creator_uid BIGINT COMMENT '创建人uid' AFTER create_time;
ALTER TABLE eh_general_approval_vals ADD COLUMN operator_uid BIGINT COMMENT '操作人Uid' AFTER creator_uid;
ALTER TABLE eh_general_approval_vals ADD COLUMN operate_time DATETIME COMMENT '编辑时间' AFTER operator_uid;

ALTER TABLE eh_general_approval_vals ADD INDEX i_eh_flow_case_id(`flow_case_id`);