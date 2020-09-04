package com.uuzuche.lib_zxing.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;


import com.uuzuche.lib_zxing.R;

/**
 * Initial the camera
 * <p>
 * 默认的二维码扫描Activity
 */
public class CaptureActivity extends Activity {
    CaptureFragment captureFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.camera);
        captureFragment = new CaptureFragment();
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getFragmentManager().beginTransaction().replace(R.id.fl_zxing_container, captureFragment).commit();
        captureFragment.setCameraInitCallBack(new CaptureFragment.CameraInitCallBack() {
            @Override
            public void callBack(Exception e) {
                if (e == null) {

                } else {
                    Log.e("TAG", "callBack: ", e);
                }
            }
        });

    }

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            //TODO 这里解析扫码后的结果，符合要求返回
            String[] temps = result.split(",");
            if (temps.length >= 2) {
                if (temps[0].equals("zhanjiu")) {
                    if (!"".equals(temps[1])) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("data", temps[1]);
                        CaptureActivity.this.setResult(RESULT_OK, resultIntent);
                        CaptureActivity.this.finish();
                        return;
                    }
                }
            }
            //重新进行扫码
            Message obtain = Message.obtain();
            obtain.what = R.id.restart_preview;
            if (null != captureFragment && null != captureFragment.getHandler()) {
                captureFragment.getHandler().sendMessageDelayed(obtain, 500);
            }
        }

        @Override
        public void onAnalyzeFailed() {
            CaptureActivity.this.finish();
        }
    };
}