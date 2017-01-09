update eh_users set update_time = now() where update_time is null;
update eh_organizations set update_time = now() where update_time is null;
update eh_organization_address_mappings set create_time = now(), update_time = now() where update_time is null;
update eh_rentalv2_orders set operate_time = now() where operate_time is null;
update eh_activities set update_time = now() where update_time is null;
update eh_office_cubicle_orders set create_time = now(), update_time = now() where update_time is null;

INSERT INTO `eh_apps` (`id`, `creator_uid`, `app_key`, `secret_key`, `name`, `description`, `status`, `create_time`, `update_uid`, `update_time`) VALUES (5001, 1, 'dca1f405-7675-4ac2-ab72-e16c918fd7c2', 'VgAddn6IGPK/gO44eUWutfjLuotMcyKz3ZpwAr2jAUSsJgIi50cvntxL4QOqgZYEXkcislDwrDmLzSeHuFWQgQ==', 'jin di idata proxy', 'jin di idata proxy', 1, '2016-11-09 11:49:16', NULL, NULL);
