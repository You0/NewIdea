package com.duanqu.Idea.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.WindowManager;


import com.duanqu.Idea.Config;

import com.R;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.yixia.camera.VCamera;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;

import static com.example.zhaoshuang.weixinrecordeddemo.MyApplication.VIDEO_PATH;

/**
 * 初始化操作，更多音乐为可配置选项。
 * Created by Mulberry on 2015/7/7.
 */
public class MyApplication extends Application{
    private static Context context;
    private static SharedPreferences sharedPreferences;

    private static HashMap<String,Handler> handlers = new HashMap<>();

    public static void setHandlers(String key,Handler handler) {
        handlers.put(key,handler);
    }

    public static Handler getHandlers(String key) {
        return handlers.get(key);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        sharedPreferences = context.getSharedPreferences("userInfo",MODE_PRIVATE);
        //初始化用户参数
        //这样写不好，应该建一个Bean
        LoadPreferences();


        Fresco.initialize(context);

        VIDEO_PATH += String.valueOf(System.currentTimeMillis());
        File file = new File(VIDEO_PATH);
        if(!file.exists()) file.mkdirs();

        //设置视频缓存路径
        VCamera.setVideoCachePath(VIDEO_PATH);

        // 开启log输出,ffmpeg输出到logcat
        VCamera.setDebugMode(true);

        // 初始化拍摄SDK，必须
        VCamera.initialize(this);

    }

    public static void LoadPreferences() {
        Config.Token = sharedPreferences.getString("Token","");
        Config.username = sharedPreferences.getString("username","");
        Config.imageurl = sharedPreferences.getString("imageurl","");
        Config.sign = sharedPreferences.getString("sign","");
        Config.headurl = sharedPreferences.getString("headurl","");
        Config.nickname = sharedPreferences.getString("nickname","");
        Config.sex = sharedPreferences.getString("sex","");
        Config.sign = sharedPreferences.getString("sign","");
        Config.grades = sharedPreferences.getString("grades","");
        Config.major = sharedPreferences.getString("major","");
        Config.school = sharedPreferences.getString("school","");
        Config.email = sharedPreferences.getString("email","");
        Config.userid = sharedPreferences.getString("userid","");
        Config.defaultHeader = "res://"+context.getPackageName()+"/"+ R.drawable.default_head_1;
        if(Config.headurl.equals("")){
            Config.headurl = "res://"+context.getPackageName()+"/"+ R.drawable.default_head_1;
        }
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }


    public static Context getContext()
    {
        return context;
    }

    public static DisplayMetrics getScreenMetrics(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    public static int dip2px( float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public static void setSharedPreferences(String key,String param)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userInfo",
                context.MODE_PRIVATE);

        sharedPreferences.edit().putString(key,param).commit();

    }

    public static void setSharedPreferences(String key,Boolean param)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userInfo",
                context.MODE_PRIVATE);

        sharedPreferences.edit().putBoolean(key,param).commit();

    }

    public static void LoadImageBySize(Uri uri, SimpleDraweeView draweeView, int width, int height){
        ImageRequest request =
                ImageRequestBuilder.newBuilderWithSource(uri)
                        .setResizeOptions(new ResizeOptions(width,height))
                        //缩放,在解码前修改内存中的图片大小, 配合Downsampling可以处理所有图片,否则只能处理jpg,
                        // 开启Downsampling:在初始化时设置.setDownsampleEnabled(true)
                        .setProgressiveRenderingEnabled(true)//支持图片渐进式加载
                        .setAutoRotateEnabled(true) //如果图片是侧着,可以自动旋转
                        .build();

        PipelineDraweeController controller =
                (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .setOldController(draweeView.getController())
                        .setAutoPlayAnimations(true) //自动播放gif动画
                        .build();

        draweeView.setController(controller);
    }


    public static File GetFrescoFile(String uri)
    {
        FileBinaryResource resource = (FileBinaryResource)Fresco.getImagePipelineFactory()
                .getMainDiskStorageCache()
                .getResource(new SimpleCacheKey(uri.toString()));

        File file = resource.getFile();
        return file;
    }

    public static String Encode(String str)
    {
        try {
            str = URLEncoder.encode(str,"UTF-8");
            str =  URLEncoder.encode(str,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String Decode(String str){
        try {
            str = URLDecoder.decode(str,"UTF-8");
            str =  URLDecoder.decode(str,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }





    private static final String AUTHTAG = "QupaiAuth";

}
