package com.example.express.activity.query;

import java.io.IOException;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.boredream.volley.BDListener;
import com.boredream.volley.BDVolley;
import com.boredream.volley.BDVolleyHttp;
import com.example.express.BaseApplication;
import com.example.express.R;
import com.example.express.activity.BaseActivity;
import com.example.express.constants.CommonConstants;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.qrcode.camera.CameraManager;
import com.qrcode.decoding.CaptureActivityHandler;
import com.qrcode.decoding.InactivityTimer;
import com.qrcode.view.ViewfinderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Initial the camera
 *
 * @author Ryan.Tang
 */
public class MipcaActivityCapture extends BaseActivity implements Callback {

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;

    private String json_result;
    private String com;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        //ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);
        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);

        initTop();
        setTitle("扫描快递单号");

        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * ����ɨ����
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        if (resultString.equals("")) {
            Toast.makeText(MipcaActivityCapture.this, "Scan failed!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
//            Intent resultIntent = new Intent();
//            Bundle bundle = new Bundle();
//            bundle.putString("result", resultString);
//            bundle.putParcelable("bitmap", barcode);
//            BaseApplication.getInstance().setResult(resultString);
//            BaseApplication.getInstance().setBitmap(barcode);
//            resultIntent.putExtras(bundle);
//            setResult(1003, resultIntent);
            queryResult(resultString);
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    /**
     * 根据运单号直接查询结果
     * @param num
     */
    private void queryResult(final String num) {
        showCustomDialog("正在查询中...");
        BDVolleyHttp.getString(CommonConstants.URLConstant + CommonConstants.MATCH_COMPANY
                        + num + CommonConstants.HTML,
                new BDListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.optBoolean("result")) {
                                String data = obj.optString("data");
                                if (isJarray(data)) {
                                    JSONArray Jarry = new JSONArray(data);
                                    JSONObject json_data = null;
                                    json_data = Jarry.getJSONObject(0);
                                    json_result = json_data.getString("name");
                                    com = json_data.getString("exname");
                                } else {
                                    JSONObject json = new JSONObject(data);
//                                    JSONObject results = json.getJSONObject("0");
                                    String name = json.getString("name");
                                    com = json.getString("exname");
                                }
                            } else {
                                showToast("很抱歉，该单号无法解析，请手动输入单号并选择快递公司");
                                finish();
                            }
                            BDVolleyHttp.getString(CommonConstants.URLConstant + CommonConstants.QUERY_EXPRESS_RESULT
                                            + num + "-" + com + CommonConstants.HTML,
                                    new BDListener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            dismissCustomDialog();
                                            Intent intent = new Intent();
                                            intent.putExtra("flag", "1");
                                            intent.putExtra("json", response);
                                            intent.setClass(MipcaActivityCapture.this, ShowResultActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                        @Override
                                        public void onErrorResponse(VolleyError volleyError) {
                                            dismissCustomDialog();
                                            Toast.makeText(MipcaActivityCapture.this, "查询失败", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            return;
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(MipcaActivityCapture.this, "查询失败", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    public static boolean isJarray(String str) {
        JSONArray Jarray;
        //JSONObject json_data = null;
        try {
            Jarray = new JSONArray(str);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            return false;

        }
        return true;
    }
}