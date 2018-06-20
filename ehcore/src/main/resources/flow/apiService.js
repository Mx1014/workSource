var javaService = Java.type("com.everhomes.flow.NashornApiService");

var apiService = {};

apiService.get = function(name) {
    return javaService.getService(name);
};

module.exports = apiService;