-- 给一碑增加app_key by st.zheng
SET @id = (SELECT MAX(id) FROM eh_apps);
INSERT INTO `eh_apps` (`id`, `creator_uid`, `app_key`, `secret_key`, `name`, `description`, `status`, `create_time`) VALUES (@id:=@id+1, '1', 'c9620212-8877-11e7-b08e-0050569605f3', 'OmnSTXMJPqvCxW8n5AmkT1xSGnJ2sWZSyWcDUi32HAD7htoLLxuzGaZUPgRN9bew6mOBW55WliSbcXRV3laC3g==', 'yibei sign', 'yibei.app', '1', now());