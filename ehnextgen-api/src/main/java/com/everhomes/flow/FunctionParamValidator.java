package com.everhomes.flow;

import com.everhomes.util.Tuple;

/**
 * Created by xq.tian on 2018/4/25.
 */
public interface FunctionParamValidator {

    Tuple<Boolean, String> validate(FuncParam funcParam, String value);

}
