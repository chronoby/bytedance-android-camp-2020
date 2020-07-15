package com.bytedance.camera.demo;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bytedance.camera.demo.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.bytedance.camera.demo.utils.Utils.MEDIA_TYPE_IMAGE;
import static com.bytedance.camera.demo.utils.Utils.MEDIA_TYPE_VIDEO;
import static com.bytedance.camera.demo.utils.Utils.getOutputMediaFile;

public class CustomCameraActivity extends AppCompatActivity {

    private SurfaceView mSurfaceView;
    private Camera mCamera;
    private File mVideoFile;
    private File mPictureFile;
    private String vpath;
    private ImageView iv;
    private VideoView vv;
    private TextView tv;

    private int CAMERA_TYPE = Camera.CameraInfo.CAMERA_FACING_BACK;
    private static final int REQUEST_EXTERNAL_STORAGE = 101;
    private boolean isRecording = false;
    private int rotationDegree = 0;

    String[] permissions = new String[] {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (Utils.isPermissionsReady(CustomCameraActivity.this,permissions)) {

        } else {
            Utils.reuqestPermissions(CustomCameraActivity.this,permissions,REQUEST_EXTERNAL_STORAGE);
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_custom_camera);
        initCamera();

        mSurfaceView = findViewById(R.id.img);
        iv = findViewById(R.id.imageView);
        vv = findViewById(R.id.videoView);
        tv = findViewById(R.id.tv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv.setVisibility(View.GONE);
                tv.setVisibility(View.GONE);
            }
        });
        vv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vv.setVisibility(View.GONE);
                tv.setVisibility(View.GONE);
            }
        });
        SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mCamera = getCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
                initCamera();
                startPreview(holder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                releaseCameraAndPreview();
            }
        });

        findViewById(R.id.btn_picture).setOnClickListener(v -> {
            mCamera.takePicture(null,null,mPicture);
        });

        findViewById(R.id.btn_record).setOnClickListener(v -> {
            if (isRecording) {
                //停止录制
                releaseMediaRecorder();
                isRecording = false;
                vv.setVisibility(View.VISIBLE);
                tv.setVisibility(View.VISIBLE);
                iv.setVisibility(View.GONE);
                vv.setVideoPath(vpath);
                vv.start();
            } else {
                //录制
                prepareVideoRecorder();
                isRecording = true;
            }
        });

        findViewById(R.id.btn_facing).setOnClickListener(v -> {
            //切换前后摄像头
            //releaseCameraAndPreview();
            if(CAMERA_TYPE == Camera.CameraInfo.CAMERA_FACING_BACK){
                mCamera = getCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);

            }else{
                mCamera = getCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
            }
                startPreview(mSurfaceView.getHolder());
        });

        findViewById(R.id.btn_zoom).setOnClickListener(v -> {
            //调焦，需要判断手机是否支持
            Camera.Parameters parameters = mCamera.getParameters();
            List<String> modes = parameters.getSupportedFocusModes(); // 判断支持
            for(String mode : modes) {
                if(mode.equals(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                    break;
                }
            }
            mCamera.setParameters(parameters);
        });

    }

    public Camera getCamera(int position) {
        CAMERA_TYPE = position;
        if (mCamera != null) {
            releaseCameraAndPreview();
        }
        Camera cam = Camera.open(position);

        //摄像头添加属性，例是否自动对焦，设置旋转方向等

        rotationDegree = getCameraDisplayOrientation(position);
        cam.setDisplayOrientation(rotationDegree);
        Camera.Parameters parameters = cam.getParameters();
        size = getOptimalPreviewSize(parameters.getSupportedPreviewSizes(),mSurfaceView.getWidth(),mSurfaceView.getHeight());
        parameters.setPreviewSize(size.width, size.height);
        return cam;
    }

    private void initCamera()
    {
        mCamera = Camera.open();
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPictureFormat(ImageFormat.JPEG);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        parameters.set("orientation", "portrait");
        parameters.set("rotation", 90);
        mCamera.setParameters(parameters);
        mCamera.setDisplayOrientation(90);
    }

    private static final int DEGREE_90 = 90;
    private static final int DEGREE_180 = 180;
    private static final int DEGREE_270 = 270;
    private static final int DEGREE_360 = 360;

    private int getCameraDisplayOrientation(int cameraId) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = DEGREE_90;
                break;
            case Surface.ROTATION_180:
                degrees = DEGREE_180;
                break;
            case Surface.ROTATION_270:
                degrees = DEGREE_270;
                break;
            default:
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % DEGREE_360;
            result = (DEGREE_360 - result) % DEGREE_360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + DEGREE_360) % DEGREE_360;
        }
        return result;
    }


    private void releaseCameraAndPreview() {
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    Camera.Size size;

    private void startPreview(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        }catch (Exception e){
            Toast.makeText(CustomCameraActivity.this,
                    "mCamera.setPreviewDisplay failed!"+e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }


    private MediaRecorder mMediaRecorder;

    private boolean prepareVideoRecorder() {
        mMediaRecorder = new MediaRecorder();
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);

        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        vpath = getOutputVideoPath();
        mMediaRecorder.setOutputFile(vpath);
        mVideoFile = getOutputMediaFile(MEDIA_TYPE_VIDEO);

        mMediaRecorder.setPreviewDisplay(mSurfaceView.getHolder().getSurface());
        mMediaRecorder.setOrientationHint(rotationDegree);

        try{
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        }catch (Exception e){
            Toast.makeText(CustomCameraActivity.this,
                    "MediaRecorder prepare Failed!"+e.getMessage(),
                    Toast.LENGTH_SHORT).show();
            releaseMediaRecorder();
            return false;
        }
        // 使得图片可以被相册找到
        MediaScannerConnection.scanFile(this, new String[] { mVideoFile.toString()},null,null);
        return true;
    }

    private String getOutputImagePath() {
        File mediaStorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir, "IMG_" + timestamp + ".jpg");
        if (!mediaFile.exists()) {
            mediaFile.getParentFile().mkdirs();
        }
        return mediaFile.getAbsolutePath();
    }

    private String getOutputVideoPath() {
        File mediaStorageDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir, "MOV_" + timestamp + ".mp4");
        if (!mediaFile.exists()) {
            mediaFile.getParentFile().mkdirs();
        }
        return mediaFile.getAbsolutePath();
    }

    private void releaseMediaRecorder() {
        mMediaRecorder.stop();
        mMediaRecorder.reset();
        mMediaRecorder.release();
        mMediaRecorder = null;
        mCamera.lock();
    }

    private Camera.PictureCallback mPicture = (data, camera) -> {
        mPictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
        FileOutputStream fos = null;
        String fpath = getOutputImagePath();
        File file = new File(fpath);
        if (mPictureFile == null) {
            return;
        }
        try {
            fos = new FileOutputStream(file);
            fos.write(data);
            fos.flush();
            Bitmap bitmap = BitmapFactory.decodeFile(fpath);
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            iv.setVisibility(View.VISIBLE);
            vv.setVisibility(View.GONE);
            tv.setVisibility(View.VISIBLE);
            iv.setImageBitmap(rotateBitmap);
        } catch (IOException e) {
            Log.d("mPicture", "Error accessing file: " + e.getMessage());
        }
        // 使得图片可以被相册找到
        MediaScannerConnection.scanFile(this, new String[] { mPictureFile.toString()},null,null);
        mCamera.startPreview();
    };


    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) h / w;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = Math.min(w, h);

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

}
