var javaService = Java.type("com.everhomes.flow.NashornConfigService");

function Config(flowId, flowVer) {
    this.flowId = flowId;
    this.flowVer = flowVer;
}

Config.prototype = {
    getString: function(key) {
        return javaService.getConfigByKey(this.flowId, this.flowVer, key);
    },
    getInt: function(key) {
        var str = this.getString(key);
        return parseInt(str);
    },
    getObject: function(key) {
        var string = this.getString(key);
        return string ? JSON.parse(string) : {};
    }
};

var configService = function(flowId, flowVer) {
    return new Config(flowId, flowVer);
};

module.exports = configService;