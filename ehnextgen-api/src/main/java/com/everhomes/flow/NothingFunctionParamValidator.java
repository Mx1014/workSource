package com.everhomes.flow;

import com.everhomes.util.Tuple;

/**
 * Created by xq.tian on 2018/4/25.
 */
public class NothingFunctionParamValidator implements FunctionParamValidator {

    @Override
    public Tuple<Boolean, String> validate(FuncParam funcParam, String value) {
        return new Tuple<>(true, "");
    }
}
