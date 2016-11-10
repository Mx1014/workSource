//load('./jvm-npm.js')
//var db = require("db");

var Apps = {};

function httpProcess(id) {
    print(id);
}

function mappingInit(id) {
    jmap = nashornObjs.get(id);
    print(jmap.getName());
    var mapName = jmap.getAppName() + "$" + jmap.getName();
    var imp = require(mapName);
    imp.mappingStart(Apps, jmap.getAppName(), jmap.getName());
}

