// @formatter:off
package com.everhomes.rest.parking;

/**
 * <p>停车场厂商</p>
 * <ul>
 * <li>BOSIGAO("BOSIGAO"): 博思高（科技园）</li>
 * <li>BOSIGAO2("BOSIGAO2"): 博思高新接口(深业)</li>
 * <li>KETUO("KETUO"): 科拓（科兴）</li>
 * <li>KETUO2("KETUO"): 科拓（储能）</li>
 * <li>WANKE("WANKE"): 万科()</li>
 * <li>INNOSPRING("INNOSPRING"): 创源</li>
 * <li>JIN_YI("JIN_YI"): 金溢(清华信息港)</li>
 * <li>ZHONG_BAI_CHANG("ZHONG_BAI_CHANG"): 中百畅(广兴源)</li>
 * <li>KEXIN_XIAOMAO("KEXIN_XIAOMAO"): 科兴正中时代广场停车场(小猫)</li>
 * <li>YINXINGZHIJIE_TECHPARK("YINXINGZHIJIE_TECHPARK"): 银星科技园停车场</li>
 * <li>YINXINGZHIJIE_XIAOMAO("YINXINGZHIJIE_XIAOMAO"): 银星工业区停车场</li>
 * <li>ELIVE_JIESHUN("ELIVE_JIESHUN"): 住总停车场</li>
 * <li>GUANG_DA_WE_GU("GUANG_DA_WE_GU"): 已不再使用，改为JIESHUN_GQY1</li>
 * <li>JIESHUN_GQY1("JIESHUN_GQY1"): 光大we谷A区停车场</li>
 * <li>JIESHUN_GQY2("JIESHUN_GQY2"): 光大we谷B区停车场</li>
 * <li>JIESHUN_DSHCXMall("JIESHUN_DSHCXMall"): 大沙河创新大厦/li>
 * <li>HKWS_SHJINMAO("HKWS_SHJINMAO"): 上海金茂停车场/li>
 * </ul>
 */
public enum ParkingLotVendor {
    BOSIGAO("BOSIGAO"),
    BOSIGAO2("BOSIGAO2"),
    KETUO("KETUO"),
    KETUO2("KETUO2"),
    WANKE("WANKE"),
    INNOSPRING("INNOSPRING"),
    JIN_YI("JIN_YI"),
    XIAOMAO("XIAOMAO"),
    MYBAY("Mybay"),
    TEST("TEST"),
    ZHONG_BAI_CHANG("ZHONG_BAI_CHANG"),
    GUANG_DA_WE_GU("GUANG_DA_WE_GU"),
    KEXIN_XIAOMAO("KEXIN_XIAOMAO"),
    YINXINGZHIJIE_XIAOMAO("YINXINGZHIJIE_XIAOMAO"),
    YINXINGZHIJIE_TECHPARK("YINXINGZHIJIE_TECHPARK"),
    ELIVE_JIESHUN("ELIVE_JIESHUN"),
    BEE_KANGLI("BEE_KANGLI"),
	BEE_ZHONGTIAN("BEE_ZHONGTIAN"),
	BEE_SUBONE("BEE_SUBONE"),
	BEE_SUBTWO("BEE_SUBTWO"),
	BEE_SUBTHREE("BEE_SUBTHREE"),
	BEE_SUBFOUR("BEE_SUBFOUR"),
	BEE_SUBFIVE("BEE_SUBFIVE"),
	JIESHUN_GQY1(VendorNameEnum.JIESHUN_GQY1),
	JIESHUN_GQY2(VendorNameEnum.JIESHUN_GQY2),
	JIESHUN_DSHCXMall(VendorNameEnum.JIESHUN_DSHCXMall),
	HKWS_SHJINMAO(VendorNameEnum.HKWS_SHJINMAO),
	KETUO_TEST("KETUO_TEST"),
	BEE_CHUANGKEGU("BEE_CHUANGKEGU"),
	NONE(""); //用于结束

    private String code;
    private ParkingLotVendor(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static ParkingLotVendor fromCode(String code) {
        if(code != null) {
            ParkingLotVendor[] values = ParkingLotVendor.values();
            for(ParkingLotVendor value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
