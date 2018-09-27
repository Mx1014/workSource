package com.everhomes.rest.yellowPage;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>NONE(0L): 无跳转</li>
 *     <li>TEMPLATE(1L): 模板提交</li>
 *     <li>MODULE(2L): 功能模块</li>
 *     <li>THIRD_URL(4L): 第三方跳转连接</li>
 * </ul>
 */
public enum JumpType {

    NONE(0L), TEMPLATE(1L), MODULE(2L), THIRD_URL(4L);

    private Long code;
    private JumpType(Long code) {
        this.code = code;
    }

    public Long getCode() {
        return this.code;
    }

    public static JumpType fromCode(Long code) {
        for(JumpType v :JumpType.values()){
            if(v.getCode().equals(code))
                return v;
        }
        return null;
    }


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
