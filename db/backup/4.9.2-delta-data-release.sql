
-- 增加一碑回调地址 by st.zheng
set @config=(select max(id) from `eh_configurations`) ;
insert into `eh_configurations` (`id`,`name`,`value`,`description`,`namespace_id`) values (@config+1,'pmtask.ebei.callback','http://core.zuolin.com/evh/pmtask/changeTaskState','callback address',999983);
