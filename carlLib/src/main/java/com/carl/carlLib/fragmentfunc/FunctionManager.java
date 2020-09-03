package com.carl.carlLib.fragmentfunc;

import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

/**
 * 方法管理器
 */
public class FunctionManager {

    private static FunctionManager instance = null;
    public static final String TAG = FunctionManager.class.getSimpleName() + "-------->";

    /**
     * 容器，用来存储方法名字 key 对应的方法名 value 对应的是 参数返回对象
     */
    private HashMap<String, FunctionWithParamWithResult> mFunctionWithParamWithResultHashMap = null;
    private HashMap<String, FunctionWithParamOnly> mFunctionWithParamsOnlyHashMap = null;
    private HashMap<String, FunctionWithResultOnly> mFunctionWithResultOnlyHashMap = null;
    private HashMap<String, FunctionNoParamNoResult> mFunctionNoParamNoResultHashMap = null;


    private FunctionManager() {
        mFunctionNoParamNoResultHashMap = new HashMap<>();
        mFunctionWithParamWithResultHashMap = new HashMap<>();
        mFunctionWithParamsOnlyHashMap = new HashMap<>();
        mFunctionWithResultOnlyHashMap = new HashMap<>();
    }


    public static FunctionManager getInstance() {
        if (null == instance) {
            instance = new FunctionManager();
        }
        return instance;
    }


    /**
     * 添加无参无返回值的方法
     *
     * @return
     */
    public FunctionManager addFunction(FunctionNoParamNoResult function) {
        mFunctionNoParamNoResultHashMap.put(function.mFunctionName, function);
        return this;
    }


    /**
     * 添加有返回值的方法
     *
     * @return
     */
    public FunctionManager addFunction(FunctionWithResultOnly function) {
        mFunctionWithResultOnlyHashMap.put(function.mFunctionName, function);
        return this;
    }


    /**
     * 添加有参数的方法
     *
     * @return
     */
    public FunctionManager addFunction(FunctionWithParamOnly function) {
        mFunctionWithParamsOnlyHashMap.put(function.mFunctionName, function);
        return this;
    }


    /**
     * 添加有参有返回值的方法
     *
     * @return
     */
    public FunctionManager addFunction(FunctionWithParamWithResult function) {
        mFunctionWithParamWithResultHashMap.put(function.mFunctionName, function);
        return this;
    }


    /**
     * 调用无返回值无参数的方法
     *
     * @param funName
     */
    public void invokeNoAll(String funName) throws NullPointerException {
        if (TextUtils.isEmpty(funName)) {
            Log.e(TAG, "funName is null !");
        } else {
            if (null != mFunctionNoParamNoResultHashMap) {
                FunctionNoParamNoResult function = mFunctionNoParamNoResultHashMap.get(funName);
                if (null != function) {
                    function.function();
                } else {
                    Log.e(TAG, "function is null !");
                }
            } else {
                throw new NullPointerException("mFunctionNoParamNoResultHashMap can not be null ,please first init FunctionManager !");
            }
        }
    }


    /**
     * 调用有参数的方法
     *
     * @param funName
     */
    public <Param> void invokeWithParamOnly(String funName, Param param) throws NullPointerException {
        if (TextUtils.isEmpty(funName)) {
            Log.e(TAG, "funName is null !");
        } else {
            if (null != mFunctionWithParamsOnlyHashMap) {
                FunctionWithParamOnly<Param> function = mFunctionWithParamsOnlyHashMap.get(funName);
                if (null != function) {
                    function.function(param);
                } else {
                    Log.e(TAG, "function is null !");
                }
            } else {
                throw new NullPointerException("mFunctionWithParamsOnlyHashMap can not be null ,please first init FunctionManager !");
            }
        }
    }


    /**
     * 调用有返回值的方法
     *
     * @param funName
     */
    public <Result> Result invokeWithResultOnly(String funName, Class<Result> c) throws NullPointerException {
        if (TextUtils.isEmpty(funName)) {
            Log.e(TAG, "funName is null !");
        } else {
            if (null != mFunctionWithResultOnlyHashMap) {
                FunctionWithResultOnly function = mFunctionWithResultOnlyHashMap.get(funName);
                if (null != function) {
                    if (null != c) {
                        return c.cast(function.function());
                    } else {
                        return (Result) function.function();
                    }
                } else {
                    Log.e(TAG, "function is null !");
                }
            } else {
                throw new NullPointerException("mFunctionWithParamsOnlyHashMap can not be null ,please first init FunctionManager !");
            }
        }
        return null;

    }

    /**
     * 调用有参数有返回值的方法
     *
     * @param funName
     */
    public <Result, Param> Result invokeWithAll(String funName, Class<Result> c, Param param) throws NullPointerException {
        if (TextUtils.isEmpty(funName)) {
            Log.e(TAG, "funName is null !");
        } else {
            if (null != mFunctionWithParamWithResultHashMap) {
                FunctionWithParamWithResult<Result, Param> function = mFunctionWithParamWithResultHashMap.get(funName);
                if (null != function) {
                    if (null != c) {
                        return c.cast(function.function(param));
                    } else {
                        return function.function(param);
                    }
                } else {
                    Log.e(TAG, "function is null !");
                }
            } else {
                throw new NullPointerException("mFunctionWithParamsOnlyHashMap can not be null ,please first init FunctionManager !");
            }
        }
        return null;
    }
}