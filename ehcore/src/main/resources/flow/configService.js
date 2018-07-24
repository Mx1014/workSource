var javaService = Java.type("com.everhomes.flow.FlowNashornConfigService");

function Config(ownerType, ownerId) {
    this.ownerType = ownerType;
    this.ownerId = ownerId;
}

Config.prototype = {
    getString: function(key) {
        return javaService.getConfigByKey(this.ownerType, this.ownerId, key);
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