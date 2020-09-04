package com.carl.carlLib.fragmentfunc;

/**
 * 有参无返回值
 */

public abstract class FunctionWithParamOnly<Param> extends Function {

    public FunctionWithParamOnly(String funName) {
        super(funName);
    }

    public abstract void function(Param param);

}