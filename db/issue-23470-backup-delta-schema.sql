-- issue-23470 by liuyilin
ALTER TABLE `eh_door_access`
ADD `enable_amount` TINYINT COMMENT '是否支持按次数开门的授权';

ALTER TABLE `eh_door_auth`
ADD `auth_rule_type` TINYINT COMMENT '授权规则的种类,0 按时间,1 按次数';
ALTER TABLE `eh_door_auth`
ADD `total_auth_amount` INT COMMENT '授权的总开门次数';
ALTER TABLE `eh_door_auth`
ADD `valid_auth_amount` INT COMMENT '剩余的开门次数';

-- End by liuyilin 
