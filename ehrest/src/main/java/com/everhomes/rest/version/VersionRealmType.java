// @formatter:off
package com.everhomes.rest.version;

/**
 * <p>版本realm类型</p>
 * <ul>
 * <li>ANDROID("Android"): 左邻Android版APP</li>
 * <li>IOS("iOS"): 左邻iOS版APP</li>
 * <li>ANDROID_TECHPARK("Android_Techpark"): 科技园Android版APP</li>
 * <li>IOS_TECHPARK("iOS_Techpark"): 科技园iOS版APP</li>
 * <li>ANDROID_XUNMEI("Android_Xunmei"): 讯美Android版APP</li>
 * <li>IOS_XUNMEI("iOS_Xunmei"): 讯美iOS版APP</li>
 * <li>ANDROID_HWPARK("Android_Hwpark"): 华为Android版APP</li>
 * <li>IOS_HWPARK("iOS_Hwpark"): 华为iOS版APP</li>
 * <li>ANDROID_ISERVICE("Android_IService"): 左邻服务Android版APP</li>
 * <li>IOS_ISERVICE("iOS_IService"): 左邻服务iOS版APP</li>
 * <li>ANDROID_SHUNICOM("Android_ShUnicom"): 上海联通Android版APP</li>
 * <li>IOS_SHUNICOM("iOS_ShUnicom"): 上海联通iOS版APP</li>
 * <li>ANDROID_JYJY("Android_JYJY"): 金隅嘉业Android版APP</li>
 * <li>IOS_JYJY("iOS_JYJY"): 金隅嘉业iOS版APP</li>
 * <li>ANDROID_VIDEOCONF("Android_Videoconf"): 视频会议Android版APP</li>
 * <li>IOS_VIDEOCONF("iOS_Videoconf"): 视频会议iOS版APP</li>
 * <li>ANDROID_LONGGANG("Android_Longgang"): 龙岗智慧社区Android版APP</li>
 * <li>IOS_LONGGANG("iOS_Longgang"): 龙岗智慧社区iOS版APP</li>
 * <li>ANDROID_HAIAN("Android_Haian"): 海岸城Android版APP</li>
 * <li>IOS_HAIAN("iOS_Haian"): 海岸城iOS版APP</li>
 * <li>H5_QUALITY("quality"): 品质核查H5</li>
 * <li>ANDROID_SHENYE("Android_Shenye"): 深业物业Android版APP</li>
 * <li>IOS_SHENYE("iOS_Shenye"): 深业物业iOS版APP</li>
 * <li>ANDROID_JINDI("Android_Jindi"): 金地商置Android版APP</li>
 * <li>IOS_JINDI("iOS_Jindi"): 金地商置iOS版APP</li>
 * <li>ANDROID_CHUNENG("Android_Chuneng"): 储能Android版APP</li>
 * <li>IOS_CHUNENG("iOS_Chuneng"): 储能iOS版APP</li>
 * <li>ANDROID_IBASE("Android_Ibase"): Ibase Android版APP</li>
 * <li>IOS_IBASE("iOS_Ibase"): Ibase iOS版APP</li>
 * <li>ANDROID_ATMINI("Android_AtMini"): 爱特家  Android版APP</li>
 * <li>IOS_ATMINI("iOS_AtMini"): 爱特家 iOS版APP</li>
 * <li>ANDROID_SZBAY("Android_SZbay"): 深圳湾 Android版APP</li>
 * <li>IOS_SZBAY("iOS_SZbay"): 深圳湾 iOS版APP</li>
 * <li>ANDROID_INNOSPRING("Android_Innospring"): innospring Android版APP</li>
 * <li>IOS_INNOSPRING("iOS_Innospring"): innospring iOS版APP</li>
 * <li>ANDROID_HUARUN("Android_Huarun"): 华润 Android版APP</li>
 * <li>IOS_HUARUN("iOS_Huarun"): 华润 iOS版APP</li>
 * <li>ANDROID_KEXIN("Android_Kexin"): 科兴 Android版APP</li>
 * <li>IOS_KEXIN("iOS_Kexin"): 科兴 iOS版APP</li>
 * <li>ANDROID_QUANZHI("Android_Quanzhi"): 全至100 Android版APP</li>
 * <li>IOS_QUANZHI("iOS_Quanzhi"): 全至100 iOS版APP</li>
 * <li>ANDROID_YUNGU("Android_YunGu"): 南山云谷 Android版APP</li>
 * <li>IOS_YUNGU("iOS_YunGu"): 南山云谷 iOS版APP</li>
 * <li>ANDROID_RONGCHAO("Android_Rongchao"): 荣超股份 Android版APP</li>
 * <li>IOS_RONGCHAO("iOS_Rongchao"): 荣超股份 iOS版APP</li>
 * <li>ANDROID_TEEC("Android_TEEC"): 嘉定新城 Android版APP</li>
 * <li>IOS_TEEC("iOS_TEEC"): 嘉定新城 iOS版APP</li>
 * <li>ANDROID_UPARK("Android_UPark"): 张江高科 Android版APP</li>
 * <li>IOS_UPARK("iOS_UPark"): 张江高科iOS版APP</li>
 * <li>ANDROID_GDWY("Android_GDWY"): 东莞互联网产业园 Android版APP</li>
 * <li>IOS_GDWY("iOS_GDWY"): 东莞互联网产业园 iOS版APP</li>
 * <li>ANDROID_GUOMAO("Android_Guomao"): 国贸</li>
 * <li>IOS_GUOMAO("iOS_Guomao"): 国贸</li>
 * <li>ANDROID_CHANGFAZHAN("Android_Changfazhan"): 昌发展</li>
 * <li>IOS_CHANGFAZHAN("iOS_Changfazhan"): 昌发展</li>
 * <li>ANDROID_MYBAY("Android_MyBay"): 深圳湾</li>
 * <li>IOS_MYBAY("iOS_MyBay"): 深圳湾</li>
 * </ul>
 */
public enum VersionRealmType {
    ANDROID("Android"),
    IOS("iOS"),
    ANDROID_TECHPARK("Android_Techpark"),
    IOS_TECHPARK("iOS_Techpark"),
    ANDROID_XUNMEI("Android_Xunmei"),
    IOS_XUNMEI("iOS_Xunmei"),
    ANDROID_HWPARK("Android_Hwpark"),
    IOS_HWPARK("iOS_Hwpark"),
    ANDROID_ISERVICE("Android_IService"),
    IOS_ISERVICE("iOS_IService"),
    ANDROID_SHUNICOM("Android_ShUnicom"),
    IOS_SHUNICOM("iOS_ShUnicom"),
    ANDROID_JYJY("Android_JYJY"),
    IOS_JYJY("iOS_JYJY"),
    ANDROID_VIDEOCONF("Android_Videoconf"),
    IOS_VIDEOCONF("iOS_Videoconf"),
    ANDROID_LONGGANG("Android_Longgang"),
    IOS_LONGGANG("iOS_Longgang"),
    ANDROID_HAIAN("Android_Haian"),
    IOS_HAIAN("iOS_Haian"),
    H5_QUALITY("quality"),
    ANDROID_SHENYE("Android_Shenye"),
    IOS_SHENYE("iOS_Shenye"),
    ANDROID_CHUNENG("Android_Chuneng"),
    IOS_CHUNENG("iOS_Chuneng"),
    ANDROID_WEIXINLINK("Android_WeixinLink"),
    IOS_WEIXINLINK("iOS_WeixinLink"),
    ANDROID_IBASE("Android_Ibase"),
    IOS_IBASE("iOS_Ibase"),
    BIZ("biz"),
    ANDROID_ATMINI("Android_AtMini"),
    IOS_ATMINI("iOS_AtMini"),
    ANDROID_SZBAY("Android_SZbay"),
    IOS_SZBAY("iOS_SZbay"),
    ANDROID_INNOSPRING("Android_Innospring"),
    IOS_INNOSPRING("iOS_Innospring"),
    WEB_ZUOLIN("Web_Zuolin"),
    WEB_PARK("Web_Park"),
    ANDROID_QINGHUA("Android_Qinghua"),
    IOS_QINGHUA("iOS_Qinghua"),
    ANDROID_TSPACE("Android_Tspace"),
    IOS_TSPACE("iOS_Tspace"),
    ANDROID_KEXIN("Android_Kexin"),
    IOS_KEXIN("iOS_Kexin"),
    ANDROID_QUANZHI("Android_Quanzhi"),
    IOS_QUANZHI("iOS_Quanzhi"),
    ANDROID_GUANGDA("Android_Guangda"),
    IOS_GUANGDA("iOS_Guangda"),
    ANDROID_KANGLI("Android_Kangli"),
    IOS_KANGLI("iOS_Kangli"),
    ANDROID_SSIPPM("Android_Ssippm"),
    IOS_SSIPPM("iOS_Ssippm"),
    ANDROID_YUNGU("Android_YunGu"),
    IOS_YUNGU("iOS_YunGu"),
    ANDROID_RONGCHAO("Android_Rongchao"),
    IOS_RONGCHAO("iOS_Rongchao"),
    ANDROID_TEEC("Android_TEEC"),
    IOS_TEEC("iOS_TEEC"),
    ANDROID_JUNMINRONGHE("Android_JunMinRongHe"),
    IOS_JUNMINRONGHE("iOS_JunMinRongHe"),
    ANDROID_BAOJIEZHIGU("Android_BaoJiEZhiGu"),
    IOS_BAOJIEZHIGU("iOS_BaoJiEZhiGu"),
    ANDROID_UPARK("Android_UPark"),
    IOS_UPARK("iOS_UPark"),
    ANDROID_GDWY("Android_GDWY"),
    IOS_GDWY("iOS_GDWY"),
    ANDROID_GUOMAO("Android_Guomao"),
    IOS_GUOMAO("iOS_Guomao"),
    ANDROID_CHANGZHIHUI("Android_ChangZhiHui"),
    IOS_CHANGZHIHUI("iOS_ChangZhiHui"),

    ANDROID_VANKE_XSH("Android_Vanke_XSH"),
    IOS_VANKE_XSH("iOS_Vanke_XSH"),
    ANDROID_OA("Android_OA"),
    IOS_OA("iOS_OA"),
    ANDROID_MYBAY("Android_MyBay"),
    IOS_MYBAY("iOS_MyBay"),

    ANDROID_XINWEILAI("Android_Xinweilai"),
    IOS_XINWEILAI("IOS_Xinweilai"),
    
    ANDROID_JINDI("Android_WeixinLink"),
    IOS_JINDI("iOS_WeixinLink"),
    ANDROID_HUARUN("Android_Officeasy"),
    IOS_HUARUN("iOS_Officeasy"),
    IOS_QIDIXIANGSHAN("iOS_QiDiXiangShan"),
    IOS_GUANGXINGYUANPARK("iOS_GuangXingYuanPark"),
    ANDROID_HUISHENGHUO("Android_HuiShenghuo"),
    IOS_HUISHENGHUO("iOS_HuiShenghuo"),

    ANDROID_CSHIDAI("Android_CShiDai"),
    IOS_CSHIDAI("iOS_CShiDai"),
    ANDROID_DASHAHEJIANTOU("Android_DaShaHeJianTou"),
    IOS_DASHAHEJIANTOU("iOS_DaShaHeJianTou"),
    ANDROID_TEFAXINXIGANG("Android_TeFaXinXiGang"),
    IOS_TEFAXINXIGANG("iOS_TeFaXinXiGang"),
    ANDROID_LUFU("Android_LuFu"),
    IOS_LUFU("iOS_LuFu"),
    ANDROID_ZHIFUHUI("Android_ZhiFuHui"),
    IOS_ZHIFUHUI("iOS_ZhiFuHui"),
    ANDROID_QIDIPARK("Android_QiDiPark"),
    IOS_QIDIPARK("iOS_QiDiPark"),
    ANDROID_GUANGXINGYUANPARK("Android_GuangXingYuanPark"),
    ANDROID_HANGZHOUYUESPACE("Android_HangZhouYueSpace"),
    IOS_HANGZHOUYUESPACE("iOS_HangZhouYueSpace"),
    ANDROID_ELIVECORE("Android_ELiveCore"),
    IOS_ELIVECORE("iOS_ELiveCore"),
    ANDROID_CHUANGYECHANG("Android_ChuangYeChang"),
    iOS_ChuangYeChang("iOS_ChuangYeChang"),
    ANDROID_QIDIXIANGSHAN("Android_QiDiXiangShan"),
    ANDROID_XINWEICHUANGYUAN("Android_XinWeiChuangYuan"),
    IOS_XINWEICHUANGYUAN("iOS_XinWeiChuangYuan"),
    ANDROID_WANZHIHUI("Android_WanZhiHui"),
    IOS_WANZHIHUI("iOS_WanZhiHui"),
    ANDROID_HUIMENGWUYE("Android_HuiMengWuYe"),
    IOS_HUIMENGWUYE("iOS_HuiMengWuYe"),
    ANDROID_GUOMAOFUWU("Android_GuoMaoFuWu"),
    IOS_GUOMAOFUWU("iOS_GuoMaoFuWu"),
    ANDROID_BEIJINGAIRPORT("Android_BeiJingAirport"),
    IOS_BEIJINGAIRPORT("iOS_BeiJingAirport"),
    ANDROID_SHUMIYUANPARK("Android_ShuMiYuanPark"),
    IOS_SHUMIYUANPARK("iOS_ShuMiYuanPark"),
    ANDROID_ZHIGUHUI("Android_ZhiGuHui"),
    IOS_ZHIGUHUI("iOS_ZhiGuHui"),
    ANDROID_BJWKZX("Android_Bjwkzx"),
	IOS_BJWKZX("iOS_Bjwkzx"),
	ANDROID_CHUANGJIHE("Android_ChuangJiHe"),
	IOS_CHUANGJIHE("iOS_ChuangJiHe"),
	ANDROID_COREDEMO("Android_CoreDemo"),
	IOS_COREDEMO("iOS_CoreDemo"),
	ANDROID_DINGFENGHUI("Android_DingFengHui"),
	IOS_DINGFENGHUI("iOS_DingFengHui"),
	ANDROID_TIANQIHUI("Android_TianQiHui"),
	IOS_TIANQIHUI("iOS_TianQiHui"),
	ANDROID_ZHIHUIYINGXING("ANDROID_ZHIHUIYINGXING"),
	IOS_ZHIHUIYINGXING("iOS_ZhiHuiYinXing"),
	ANDROID_ZIBENHUI("Android_ZiBenHui"),
	IOS_ZIBENHUI("iOS_ZiBenHui"),
	ANDROID_XIXIANDAXIA("Android_XiXianDaXia"),
	IOS_XIXIANDAXIA("iOS_XiXianDaXia"),
	ANDROID_GUOMAOWUFUWX("Android_GuoMaoWuFuwx"),
	IOS_GUOMAOWUFUWX("iOS_GuoMaoWuFuwx"),
	ANDROID_ZHONGKERUIGU("Android_ZhongKeRuiGu"),
	IOS_ZHONGKERUIGU("iOS_ZhongKeRuiGu"),
	
	ANDROID_FUNDTOWN("Android_FundTown"),
	IOS_FUNDTOWN("iOS_FundTown"),
	ANDROID_BIZ("Android_Biz"),
	IOS_BIZ("iOS_Biz"),

    ANDROID_ZHANGSHANGYUQUAN("Android_ZhangShangYuQuan"),
    IOS_ZHANGSHANGYUQUAN("iOS_ZhangShangYuQuan"),
    ANDROID_KUNTAIHOTAL("Android_KunTaiHotal"),
    IOS_KUNTAIHOTAL("iOS_KunTaiHotal"),
    ANDROID_CHUANGXINYUNGU("Android_ChuangXinYunGu"),
    IOS_CHUANGXINYUNGU("iOS_ChuangXinYunGu"),
    ANDROID_OLINK("Android_OLink"),
    IOS_OLINK("iOS_OLink"),
    ANDROID_XINGFUNUODE("Android_XingFuNuoDe"),
    IOS_XINGFUNUODE("iOS_XingFuNuoDe"),
    ANDROID_ZHENZHIHUI("Android_ZhenZhiHui"),
    IOS_ZHENZHIHUI("iOS_ZhenZhiHui"),
    ANDROID_ZHENJIASU("Android_ZhenJiaSu"),
    IOS_ZHENJIASU("iOS_ZhenJiaSu"),

    ANDROID_YUESPACE("Android_YueSpace"),
    IOS_YUESPACE("iOS_YueSpace"),
    ANDROID_INNOPLUS("Android_InnoPlus"),
    IOS_INNOPLUS("iOS_InnoPlus"),
    ANDROID_JINGUTONG("Android_JinGuTong"),
    IOS_JINGUTONG("iOS_JinGuTong"),
    ANDROID_BILINSHE("Android_BiLinShe"),
    IOS_BILINSHE("iOS_BiLinShe")
    ;

    private String code;
    private VersionRealmType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static VersionRealmType fromCode(String code) {
        if(code != null) {
            for(VersionRealmType value : VersionRealmType.values()) {
                if(code.equalsIgnoreCase(value.code)) {
                    return value;
                }
            }
        }

        return null;
    }
}
