--
-- 通用脚本
-- ADD BY xq.tian  2018/06/15
-- #30750 代码仓库管理 v1.0
--
SET @eh_configurations_id = (SELECT MAX(id) FROM eh_configurations);
INSERT INTO ehcore.eh_configurations (id, name, value, description, namespace_id, display_name)
  VALUES ((@eh_configurations_id := @eh_configurations_id + 1), 'gogs.server.url', 'http://10.1.10.60:3000/api/v1', 'gogs server', 0, '');
INSERT INTO eh_configurations (id, name, value, description, namespace_id, display_name)
  VALUES ((@eh_configurations_id := @eh_configurations_id + 1), 'gogs.admin.name', 'zuolin-project', 'gogs admin name', 0, '');
INSERT INTO eh_configurations (id, name, value, description, namespace_id, display_name)
  VALUES ((@eh_configurations_id := @eh_configurations_id + 1), 'gogs.admin.token', 'afe3b2a0165958086f7cceed7843190c15197c08', 'gogs admin token', 0, '');

-- #30750 END