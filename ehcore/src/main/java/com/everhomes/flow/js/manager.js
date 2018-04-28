function process(id) {
    var obj = nashornObjs.get(id);

    var moduleApp =
        require(["nashorn", obj.moduleId, obj.organizationId, obj.funcName, obj.funcVersion].join(":"));



}