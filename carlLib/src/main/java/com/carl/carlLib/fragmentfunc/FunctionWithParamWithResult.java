package com.carl.carlLib.fragmentfunc;

/**
 * 有参有返回值
 */

public abstract class FunctionWithParamWithResult<Result, Param> extends Function {

    public FunctionWithParamWithResult(String funName) {
        super(funName);
    }

    public abstract Result function(Param param);

}