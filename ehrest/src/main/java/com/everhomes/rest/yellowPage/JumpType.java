package com.everhomes.rest.yellowPage;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>NONE(0L): 无跳转</li>
 *     <li>TEMPLATE(1L): 模板提交(已不使用)</li>
 *     <li>FORM(2L): 表单/表单+工作流</li>
 *     <li>APPLICATION(3L): 转应用</li>
 *     <li>THIRD_URL(4L): 第三方跳转连接</li>
 * </ul>
 */
public enum JumpType {

    NONE(0L), TEMPLATE(1L), FORM(2L), APPLICATION(3L), THIRD_URL(4L);

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
