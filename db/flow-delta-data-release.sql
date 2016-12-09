INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'flow.stepname.start_step', '开始', 'start-step');
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'flow.stepname.approve_step', '下一步', 'approve-step');
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'flow.stepname.reject_step', '驳回', 'reject-step');
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'flow.stepname.transfer_step', '转交', 'transfer-step');
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'flow.stepname.comment_step', '附言', 'comment-step');
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'flow.stepname.absort_step', '终止', 'absort-step');
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'flow.stepname.reminder_step', '催办', 'reminder-step');
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'flow.stepname.evaluate_step', '评价', 'evaluate-step');
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'flow.stepname.end_step', '结束', 'end-step');

INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES('flow', '10002', 'zh_CN', '激活失败');

INSERT INTO `ehcore_aclink`.`eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('1000', '0', '0', '', '0', '', 'applierName', '发起人姓名', 'text', 'bean_id', 'flow-variable-applier-name', '1');

INSERT INTO `ehcore_aclink`.`eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('1001', '0', '0', '', '0', '', 'applierPhone', '发起人手机号码', 'text', 'bean_id', 'flow-variable-applier-phone', '1');

INSERT INTO `ehcore_aclink`.`eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('1002', '0', '0', '', '0', '', 'currProcessorName', '本节点处理人姓名', 'text', 'bean_id', 'flow-variable-curr-processor-name', '1');

INSERT INTO `ehcore_aclink`.`eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('1003', '0', '0', '', '0', '', 'currProcessorPhone', '本节点处理人手机号码', 'text', 'bean_id', 'flow-variable-curr-processor-phone', '1');
