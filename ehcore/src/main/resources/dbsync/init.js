//load('./jvm-npm.js')
//var db = require("db");

var Apps = {};

function doResp(jreq, obj) {
    jreq.resp(JSON.stringify(obj))
}

function httpProcess(id) {
    jreq = nashornObjs.get(id);
    var app = Apps[jreq.getAppName()];
    if(!app) {
        print("app not found");
        jreq.resp("app not found");
        return;
    }
    var query = app.queries[jreq.getMapName()];
    if(!query || !query[jreq.getQuery()]) {
        print("query not found");
        jreq.resp("query not found");
        return;
    }
    //var body = JSON.parse(jreq.getBody());

    print("makeQuery...");
    rlt = nashornObjs.makeQuery(jreq.getAppName(), jreq.getMapName(), jreq.getQuery(), jreq.getBody())
    print("rlt=" + rlt);
    //TODO more convert
    jreq.resp(rlt);
}

function mappingInit(id) {
    jmap = nashornObjs.get(id);
    print("initial:" + jmap.getName());
    //use prefix to identify that it's a mapping resource
    var mapName = "app$" + jmap.getAppName() + "$" + jmap.getName();
    var imp = require(mapName);
    imp.mappingStart(Apps, jmap.getAppName(), jmap.getName());
}

