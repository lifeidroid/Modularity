package com.carl.carlLib.fragmentfunc;

/**
 *  无参有返回值
 */

public abstract class FunctionWithResultOnly<Result> extends Function {

    public FunctionWithResultOnly(String funName) {
        super(funName);
    }

    public abstract Result function();

}