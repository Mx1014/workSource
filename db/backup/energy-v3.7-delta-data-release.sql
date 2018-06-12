-- 能耗更新离线包版本   by jiarui
update eh_version_urls set download_url = replace(download_url,'1-0-1','1-0-3'),
  info_url = replace(info_url,'1-0-1','1-0-3'),
  target_version = '1.0.3' where realm_id = (select id from eh_version_realm where realm = 'energyManagement');

