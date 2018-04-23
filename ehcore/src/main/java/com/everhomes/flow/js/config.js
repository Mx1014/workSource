var config = {};

var configService = Java.type("com.everhomes.flow.NashornConfigService");

config.String = function(key) {
    return configService.getConfigByKey(key);
};

config.Int = function(key) {
    var str = String(key)
    return parseInt(str);
};

config.Object = function(key) {
    return parseJSON(String(key))
};

modules.export = config;