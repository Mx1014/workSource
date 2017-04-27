-- 提示信息 add by sfyan 20170427
insert into `eh_locale_strings` (`scope`, `code`, `locale`, `text`) values('organization','100010','zh_CN','手机号码已存在');
insert into `eh_locale_strings` (`scope`, `code`, `locale`, `text`) values('organization','700000','zh_CN','姓名不能为空');
insert into `eh_locale_strings` (`scope`, `code`, `locale`, `text`) values('organization','700001','zh_CN','电话号码不能为空');

-- 把eh_organization_members的两个字段更新对应到eh_organization两个字段的值 add by sfyan 20170427
update `eh_organization_members` om set `group_path` = (select path from eh_organizations where id = om.organization_id), `group_type` = (select `group_type` from eh_organizations where id = om.organization_id), `update_time` = now();