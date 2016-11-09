//load('./jvm-npm.js')
var h = require("db");
h.hello();

function httpProcess(id) {
    print(id);
}

function mappingInit(id) {
    mapItem = nashornObjs.get(id);
    print(mapItem.getName());
}

