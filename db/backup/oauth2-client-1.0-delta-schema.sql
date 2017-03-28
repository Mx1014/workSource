-- oauth2client 1.0   add by xq.tian 2017/03/09
--
-- oauth2 client AccessToken
--
DROP TABLE IF EXISTS `eh_oauth2_client_tokens`;
CREATE TABLE `eh_oauth2_client_tokens` (
  `id` BIGINT NOT NULL,
  `token_string` VARCHAR(128) NOT NULL COMMENT 'token string issued to requestor',
  `vendor` VARCHAR(32) NOT NULL COMMENT 'OAuth2 server name',
  `grantor_uid` BIGINT NOT NULL COMMENT 'eh_users id',
  `expiration_time` DATETIME NOT NULL COMMENT 'a successful acquire of access token by the code should immediately expires it',
  `scope` VARCHAR(256) NULL DEFAULT NULL COMMENT 'space-delimited scope tokens per RFC 6749',
  `type` TINYINT NOT NULL DEFAULT '0' COMMENT '0: access token, 1: refresh token',
  `create_time` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHAR SET ='utf8mb4';


--
-- oauth2 servers
--
DROP TABLE IF EXISTS `eh_oauth2_servers`;
CREATE TABLE `eh_oauth2_servers` (
  `id` BIGINT NOT NULL,
  `vendor` VARCHAR(32) NOT NULL COMMENT 'OAuth2 server name',
  `client_id` VARCHAR(128) NOT NULL COMMENT 'third part provided',
  `client_secret` VARCHAR(128) NOT NULL COMMENT 'third part provided',
  `redirect_uri` VARCHAR(1024) NOT NULL COMMENT 'authorize success redirect to this url',
  `response_type` VARCHAR(128) NOT NULL COMMENT 'e.g: code',
  `grant_type` VARCHAR(128) NOT NULL COMMENT 'e.g: authorization_code',
  `state` VARCHAR(128) NOT NULL COMMENT 'e.g: OAuth server will response this filed original',
  `scope` VARCHAR(256) NULL DEFAULT NULL COMMENT 'space-delimited scope tokens per RFC 6749',
  `authorize_url` VARCHAR(1024) NULL DEFAULT NULL COMMENT 'OAuth server provided authorize url',
  `token_url` VARCHAR(1024) NULL DEFAULT NULL COMMENT 'OAuth server provided get token url',
#   `service_url` VARCHAR(1024) NULL DEFAULT NULL COMMENT 'OAuth server provided authorize url',
  `create_time` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHAR SET ='utf8mb4';
