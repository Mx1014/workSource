package com.everhomes.buttscript;

public class ButtScriptConstants {


    /**
     * 用于定义每个仓库中的文件路径前缀 ,文件路径定义为:repo_999957(repo_+域空间ID)
     * 每一种infotype 拥有一个仓库,靠文件路径来区分不同的脚本,每个域空间都有自己的文件路径
     * 也就是说eh_butt_script_config 这张表中的数据,只要info_type字段是相同的话,module_id/module_type/owner_id/owner_type也是相同的
     */
    public static final String PRE_PATH = "repo_";

    /**
     * 在此功能中创建仓库时所用入参namespaceID皆为0,也就是不按域空间来区分库,
     * 而是按eh_butt_script_config 中的info_type来区分,避免创建多个库.因为有可能
     * 多个域空间都有同一个info_type.为方便管理,把它们放到同一个库中.不同的域空间的
     * 脚本在该空间下靠path区分.
     */
    public static final Integer GOS_NAMESPACEID = 0;
}
