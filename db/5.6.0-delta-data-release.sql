-- 能耗管理升级离线包 by  jiarui 20180619
update eh_version_urls set download_url = replace(download_url,'1-0-3','1-0-4'),
  info_url = replace(info_url,'1-0-3','1-0-4'),
  target_version = '1.0.3' where realm_id = (select id from eh_version_realm where realm = 'energyManagement');