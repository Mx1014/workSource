update eh_parking_lots SET config_json = REPLACE(config_json,'}',',"identityCardFlag":0}') WHERE owner_id <> 240111044331055940;
update eh_parking_lots SET config_json = REPLACE(config_json,'}',',"identityCardFlag":1}') WHERE owner_id = 240111044331055940;
