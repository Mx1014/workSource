/**
 *Designer:yanlong Liang
 *Description:短信白名单
 *Created: 2018-5-22
 */
CREATE TABLE eh_phone_white_list (
	id BIGINT NOT NULL COMMENT '主键',
	namespace_id INT NOT NULL DEFAULT 0 COMMENT '域空间',
	phone_number VARCHAR(128) NOT NULL COMMENT '白名单手机号码',
	creator_uid BIGINT COMMENT '记录创建人userID',
	creator_time DATETIME COMMENT '记录创建时间',

	PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb64 COMMENT '短信白名单';


/**
 * End by: yanlong Liang
 */