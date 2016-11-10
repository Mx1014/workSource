var App = require("db");

map = {
    "tables": ["eh_user_identifiers", "eh_users"],
    "eh_user_identifiers": {
        "fields": ["id", "owner_uid", "identifier_type", "name", "claim_status"],
        "primary": "id",
        "belong": [
            {
            "table": "eh_users",
            "fieldA": "owner_uid",
            "fieldB": "id",
            "join": "INNER_JOIN"
            }
        ],
        "hasMany": [],
        "hasOne": []
    },
    "eh_users": {
        "fields": ["id", "uuid", "account_name", "nick_name"],
        "primary": "id"
    }
};

query = {
    "getByUserId": {
        "tableName": "eh_user_identifiers",
        "conditions": [" eh_users.id = $userId ", " eh_user_identifiers.claim_status = $claimStatus "],
        "defaults": {
            "claimStatus": 3
        }
    }
};

var mapping = {};
mapping.mappingStart = function(apps, appName, mapName) {
    var app = apps[appName];
    if(!app) {
        app = new App(appName)
        apps[appName] = app;
    }

    app.maps[mapName] = map;
    app.queries[mapName] = query;

    var graphName = appName + "$" + mapName;
    graph = nashornObjs.getGraph(graphName);
    if(!graph) {
        var obj = {};
        obj["appName"] = appName;
        obj["mapName"] = mapName;
        obj["mapping"] = map;
        obj["query"] = query;
        graph = nashornObjs.createGraph(JSON.stringify(obj));
    }
}

module.exports = mapping;
