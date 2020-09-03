package cn.carl.communicationLib.ble;

import java.io.Serializable;

/**
 * ==============================================
 * author : carl
 * e-mail : 991579741@qq.com
 * time   : 2018/12/11
 * desc   :
 * version: 1.0
 * ==============================================
 */
public interface BleDecodeInterface extends Serializable {
    /**
     * 解析数据
     * @param data
     * @param decodeResult
     */
    void decodeData(String data, DecodeResult decodeResult);

    /**
     * 解析完成后的回调
     */
    interface DecodeResult {
        /**
         * 解析结果回调
         *
         * @param msg 解析成功的消息
         */
        void onResult(String msg);
    }
}
