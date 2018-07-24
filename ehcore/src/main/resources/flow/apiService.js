var javaService = Java.type("com.everhomes.scriptengine.nashorn.NashornApiService");

var apiService = {};

apiService.get = function(name) {
    return javaService.getService(name);
};

module.exports = apiService;