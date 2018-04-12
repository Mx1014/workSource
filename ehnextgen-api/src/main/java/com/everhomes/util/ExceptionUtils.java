//@formatter:off
package com.everhomes.util;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.organization.OrganizationJobPosition;

import javax.management.*;
import javax.management.RuntimeErrorException;

/**
 * Created by Wentian Wang on 2018/3/8.
 */

public class ExceptionUtils {
    /**
     *
     * @param obj 需要检查的对象
     * @param name 对象的字段名称，用于异常提示
     */
    public static void nullProhibited(Object obj, String name){
        if(obj == null){
            throw com.everhomes.util.RuntimeErrorException.errorWith(
                    ErrorCodes.SCOPE_GENERAL
                    ,ErrorCodes.ERROR_INVALID_PARAMETER,
                    "parameter "+name+" cannot be null, please checkout if it had been sent correctly to backend"
            );
        }
    }
}
