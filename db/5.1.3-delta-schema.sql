ALTER TABLE eh_parking_card_requests ADD column identity_card VARCHAR(40);

-- binding log id add by xq.tian  2018/01/25
ALTER TABLE eh_point_logs ADD COLUMN binding_log_id BIGINT NOT NULL DEFAULT 0;