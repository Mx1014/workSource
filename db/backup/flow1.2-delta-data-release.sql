-- add more variables by janson
INSERT INTO `eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('1004', '0', '0', '', '0', '', 'currAllProcessorsName', '本节点处理人姓名', 'text', 'bean_id', 'flow-variable-curr-all-processors-name', '1');

INSERT INTO `eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('1005', '0', '0', '', '0', '', 'currAllProcessorsPhone', '本节点处理人手机号码', 'text', 'bean_id', 'flow-variable-curr-all-processors-phone', '1');

INSERT INTO `eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('1006', '0', '0', '', '0', '', 'prefixAllProcessorsName', '上一步处理人姓名', 'text', 'bean_id', 'flow-variable-prefix-all-processors-name', '1');

INSERT INTO `eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('1007', '0', '0', '', '0', '', 'prefixAllProcessorsPhone', '上一步处理人手机号码', 'text', 'bean_id', 'flow-variable-prefix-all-processors-phone', '1');

INSERT INTO `eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('1008', '0', '0', '', '0', '', 'transferTargetName', '被转交人姓名', 'text', 'bean_id', 'flow-variable-transfer-target-name', '1');

INSERT INTO `eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('1009', '0', '0', '', '0', '', 'transferTargetPhone', '被转交人手机号', 'text', 'bean_id', 'flow-variable-transfer-target-phone', '1');

UPDATE `eh_flow_variables` SET `label`='本节点操作执行人姓名' WHERE `id`='1002';
UPDATE `eh_flow_variables` SET `label`='本节点操作执行人手机号' WHERE `id`='1003';

UPDATE `eh_flow_variables` SET `label`='上一个节点执行人' WHERE `id`='2001';
UPDATE `eh_flow_variables` SET `label`='本节点执行人' WHERE `id`='2002';

INSERT INTO `eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('2007', '0', '0', '', '0', '', 'prefixProcessors', '上个节点处理人', 'node_user', 'bean_id', 'flow-variable-prefix-node-processors', '1');

INSERT INTO `eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('2008', '0', '0', '', '0', '', 'currProcessors', '本节点处理人', 'node_user', 'bean_id', 'flow-variable-curr-node-processors', '1');

INSERT INTO `eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('2009', '0', '0', '', '0', '', 'targetTransfer', '被转交人', 'node_user', 'bean_id', 'flow-variable-target-node-transfer', '1');

