package cn.carl.communicationLib.bt;

import java.io.Serializable;


/**
 * ==============================================
 * author : carl
 * e-mail : 991579741@qq.com
 * time   : 2018/12/10
 * desc   : Socket 数据解析模板
 * version: 1.0
 * ==============================================
 */
public interface BtDecodeInterface extends Serializable {
    /**
     * 解析消息
     * @param clientThread
     * @param data
     * @param decodeResult
     */
    void decodeData(BtClientThread clientThread, String data, DecodeResult decodeResult);

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
