var config = {};

// var configService = Java.type("com.everhomes.flow.NashornConfigService");

config.String = function(key) {
    // return configService.getConfigByKey(key);
    return "1";
};

config.Int = function(key) {
    var str = String(key);
    return parseInt(str);
};

config.Object = function(key) {
    return JSON.parse(String(key))
};

config.register = function (data) {
    // configService.registerConfig(data);
};

module.exports = config;