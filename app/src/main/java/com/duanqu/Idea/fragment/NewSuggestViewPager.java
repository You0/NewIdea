package com.duanqu.Idea.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.R;
import com.duanqu.Idea.Adapter.CotainViewPager;
import com.duanqu.Idea.Config;
import com.duanqu.Idea.CustomView.VideoPlayView;
import com.duanqu.Idea.JsonParse.BaiSi;
import com.duanqu.Idea.JsonParse.MainMessageParse;
import com.duanqu.Idea.activity.MainActivity1;
import com.duanqu.Idea.activity.VideoPlayViewActivity;
import com.duanqu.Idea.app.MyApplication;
import com.duanqu.Idea.bean.BaiSiBean;
import com.duanqu.Idea.bean.MainMessageBean;
import com.duanqu.Idea.bean.VideoInfo;
import com.duanqu.Idea.test.Datas;
import com.duanqu.Idea.utils.MyGestureDetector;
import com.facebook.drawee.view.SimpleDraweeView;
import com.uiadapter.NewMainActivity;
import com.yixia.camera.util.Log;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.LinkedList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by Me on 2017/4/7.
 */

public class NewSuggestViewPager extends BaseFragment implements VideoPlayView.MediaPlayerImpl, AbsListView.OnScrollListener {
    private View view;
    private ListView videoList;
    private MyAdapter adapter;
    private VideoPlayView playView;
    private View currentItemView;
    private int currentPosition = -1;

    private LinkedList<VideoInfo> path;
    private boolean isPlaying;
    private int firstVisiblePosition;
    private LinearLayout linearLayout;
    private MainMessageBean Messageinfo;
    private LinkedList<BaiSiBean> beans = new LinkedList<>();
    private String url;
    private String maxtime;
    private Boolean new_v;
    private boolean Visibility = true;

    private boolean isAnimtion = false;

    private int VisibleCount = -1;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:{
                    //去除点击进入的视频之前的视频内容。
                    //CheckPathAndBean();
                    adapter.setData(path);
                    adapter.notifyDataSetChanged();
                    loading = false;
                    break;
                }
            }
        }
    };



    private void initView() {


        videoList = (ListView) view.findViewById(R.id.video_watch_list);

    }

    private void initData() {
        adapter = new MyAdapter(context);
        videoList.setAdapter(adapter);
        videoList.setOnScrollListener(this);


        videoList.setOnTouchListener(new MyGestureDetector(getActivity()) {
            @Override
            public void onScrollDown() {
                if (Visibility == false) {
                    Animation DisplayAnimation = new TranslateAnimation(0, 0, MyApplication.dip2px(80), 0);
                    DisplayAnimation.setDuration(300);
                    DisplayAnimation.setFillAfter(true);

                    NewMainActivity.bottomBar.startAnimation(DisplayAnimation);
                    NewMainActivity.bottomBar.setVisibility(View.VISIBLE);
                    Visibility = true;
                }
            }

            @Override
            public void onScrollUp() {
                if (Visibility == true) {
                    Animation MissAnimation = new TranslateAnimation(0, 0, 0, MyApplication.dip2px(80));
                    MissAnimation.setDuration(300);
                    MissAnimation.setFillAfter(true);
                    NewMainActivity.bottomBar.startAnimation(MissAnimation);
                    NewMainActivity.bottomBar.setVisibility(View.GONE);
                    Visibility = false;
                }
            }
        });




        path = new LinkedList<VideoInfo>();

        if(Messageinfo!=null){
            VideoInfo videoInfo = new VideoInfo(
                    Messageinfo.getVideoUri(), (String) Messageinfo.getImages().get(0),
                    Messageinfo.getTextContent(),"",1,Messageinfo);
            path.add(videoInfo);
        }

        GetFriendsVideoFeedFromServer();
    }


    //    //通过 set 方法在适配器中拿到这三个参数
    public void setPlayView(VideoPlayView playView) {
        this.playView = playView;
        playView.setMediaPlayerListenr(this);
        Log.i("XX", currentPosition + "");
        int[] curr = new int[2];
        currentItemView.getLocationOnScreen(curr);
        Log.i("TAG", curr[1] + "");
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.i("EE", "当前" + currentPosition);
        Log.i("EE", "可见的第一个" + videoList.getFirstVisiblePosition());

        //mNavigateTabBar.setVisibility(View.GONE);


        if(videoList.getChildAt(0)==null){
            return;
        }

        if(getVisibilityPercents(videoList.getChildAt(0))<=80)
        {
            int next =1;
            final MyAdapter.ViewHolder viewHolder = (MyAdapter.ViewHolder) videoList.getChildAt(0).getTag();
            final MyAdapter.ViewHolder viewHolder1 = (MyAdapter.ViewHolder) videoList.getChildAt(next).getTag();

            if (viewHolder.black_cover.getVisibility() == View.GONE && isAnimtion==false) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewHolder.setBlack_cover();
                        //closeVideo();

                    }
                }, 1);
            }

            if(viewHolder1.black_cover.getVisibility()!=View.GONE && isAnimtion==false){
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewHolder1.setBlack_coverGone();
                    }
                }, 1);
            }
        }else{

            int next =1;

            final MyAdapter.ViewHolder viewHolder = (MyAdapter.ViewHolder) videoList.getChildAt(0).getTag();


            if (viewHolder.black_cover.getVisibility() != View.GONE && isAnimtion==false) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewHolder.setBlack_coverGone();
                    }
                }, 1);
            }

            Log.e("getChildCount","count:"+videoList.getChildCount());
            if(videoList.getChildCount()<=1){
                return;
            }


            final MyAdapter.ViewHolder viewHolder1 = (MyAdapter.ViewHolder) videoList.getChildAt(next).getTag();


            if(viewHolder1.black_cover.getVisibility()==View.GONE && isAnimtion==false){
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewHolder1.setBlack_cover();
                        //closeVideo();
                    }
                }, 1);
            }

        }



        //videoList.getChildAt(0);
        System.out.println("百分比" + videoList.getFirstVisiblePosition() + ":" + getVisibilityPercents(videoList.getChildAt(0)));
        //划出屏幕时关闭
        System.out.println("百分比+bottom" + getVisibilityPercents(videoList.getChildAt(visibleItemCount - 1)));

        if ((currentPosition < videoList.getFirstVisiblePosition() || currentPosition > videoList
                .getLastVisiblePosition()) && isPlaying && getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT) {
            closeVideo();
        }

        if(videoList != null && videoList.getChildCount() > 0){
            if(firstVisibleItem + visibleItemCount == totalItemCount)
            {
                Log.e("VideoPlayViewActivity","LoadMore");
                LoadMore();
            }
        }





    }





    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.new_suggest_fragment,null);
        initView();
        initData();
        return view;
    }

    private void closeVideo() {
        currentPosition = -1;
        isPlaying = false;
        playView.stop();
        adapter.notifyDataSetChanged();
        playView = null;
        currentItemView = null;
    }

    /**
     * 在 manifest 中设置当前 activity, 当横竖屏切换时会执行该方法, 否则会 finish 重新执行一遍生命周期
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (playView != null && newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            // scrollDistance = currentItemView.getTop();

            //获取状态栏高度(如果设置沉浸式状态栏了就不需要获取)
            Rect rect = new Rect();
            getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            currentItemView.setLayoutParams(new AbsListView.
                    LayoutParams(ListView.LayoutParams.MATCH_PARENT,
                    getActivity().getWindowManager().getDefaultDisplay().getHeight() - rect.top));
            //设置横屏后要显示的当前的 itemView
            videoList.post(new Runnable() {
                @Override
                public void run() {
                    //一定要对添加这句话,否则无效,因为界面初始化完成后 listView 失去了焦点
                    videoList.requestFocusFromTouch();
                    videoList.setSelection(currentPosition);
                }
            });
            Log.i("XX", "横屏");
        } else if (playView != null && newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //横屏时的设置会影响返回竖屏后的效果, 这里设置高度与 xml 文件中的高度相同
            Log.i("MM", currentPosition + "竖屏");
            currentItemView.setLayoutParams(new AbsListView.LayoutParams(
                    ListView.LayoutParams.MATCH_PARENT, getResources()
                    .getDimensionPixelOffset(R.dimen.itmes_height)));
            //本来想切换到竖屏后恢复到初始位置,但是上部出现空白
//            videoList.scrollBy(0, -(scrollDistance));
            //通过该方法恢复位置,不过还是有点小问题
            videoList.post(new Runnable() {
                @Override
                public void run() {
                    videoList.requestFocusFromTouch();
                    videoList.setSelection(firstVisiblePosition);
                }
            });
            Log.i("XX", "竖屏");
        }
    }

    @Override
    public void onError() {
        closeVideo();
    }

    @Override
    public void onExpend() {

    }

    @Override
    public void onShrik() {

    }


    public int getVisibilityPercents(View view) {
        if (view == null) {
            return -1;
        }

        Rect rect = new Rect();
        View tv = view;
        tv.getLocalVisibleRect(rect);
        int height = tv.getHeight();
        int percents = 100;
        if (rect.top == 0 && rect.bottom == height) {
            percents = 100;
        } else if (rect.top > 0) {
            percents = (height - rect.top) * 100 / height;
        } else if (rect.bottom > 0 && rect.bottom < height) {
            percents = rect.bottom * 100 / height;
        }
        return percents;
    }


    //适配器

    private class MyAdapter extends BaseAdapter {

        private Context mContext;

        private List<VideoInfo> videoPaths;

        public MyAdapter(Context mContext) {
            this.mContext = mContext;
        }

        public void setData(List<VideoInfo> videoPaths) {
            this.videoPaths = videoPaths;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return videoPaths != null && videoPaths.size() > 0 ? videoPaths.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return videoPaths != null && videoPaths.size() > 0 ? videoPaths.get(position) : null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.black_cover, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.play_btn = (ImageView) convertView.findViewById(R.id.play_btn);
                viewHolder.play_view = (VideoPlayView) convertView.findViewById(R.id.video_play_view);
                viewHolder.black_cover = convertView.findViewById(R.id.black_cover);
                viewHolder.cover_image = (SimpleDraweeView) convertView.findViewById(R.id.cover_image);
                viewHolder.title = (TextView) convertView.findViewById(R.id.title);
                viewHolder.play_linearlayout = (LinearLayout) convertView.findViewById(R.id.play_linearlayout);
                viewHolder.userid = (TextView) convertView.findViewById(R.id.userid);
                viewHolder.comments = (TextView) convertView.findViewById(R.id.comments);
                viewHolder.head = (SimpleDraweeView) convertView.findViewById(R.id.head);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }


            viewHolder.cover_image.setImageURI(Uri.parse(videoPaths.get(position).getCover_Url()));

            viewHolder.play_btn.setOnClickListener(new MyClick(viewHolder, convertView, position));
            viewHolder.title.setText(videoPaths.get(position).getTitle());

            if (videoPaths.get(position).getScale() == 2) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        (int) Config.WIDTH/5*4);
                viewHolder.play_linearlayout.setLayoutParams(params);
            } else {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        (int)Config.HEIGHT/5*4);
                viewHolder.play_linearlayout.setLayoutParams(params);
            }


            if (currentPosition == position) {
                viewHolder.play_view.setVisibility(View.VISIBLE);
            } else {
                viewHolder.play_view.setVisibility(View.GONE);
                viewHolder.play_view.stop();
                viewHolder.cover_image.setVisibility(View.VISIBLE);
                viewHolder.play_btn.setVisibility(View.VISIBLE);
            }

            viewHolder.userid.setText("");
            viewHolder.comments.setText("");
            viewHolder.head.setImageURI("");


            return convertView;
        }
        private class ViewHolder {
            private ImageView play_btn;//播放按钮
            private View black_cover;
            private SimpleDraweeView cover_image;
            private VideoPlayView play_view;
            private TextView title;
            private LinearLayout play_linearlayout;
            private TextView userid;
            private SimpleDraweeView head;
            private TextView comments;


            public void setBlack_coverGone() {
                Animation alphaAnimation = new AlphaAnimation(1, 0);
                alphaAnimation.setDuration(100);
                alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        black_cover.setVisibility(View.GONE);
                        isAnimtion = false;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                isAnimtion = true;
                black_cover.startAnimation(alphaAnimation);

                //black_cover.setVisibility(View.GONE);
            }

            public void setBlack_cover() {
                Animation alphaAnimation = new AlphaAnimation(0, 1);
                alphaAnimation.setDuration(100);
                alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        black_cover.setVisibility(View.VISIBLE);
                        isAnimtion = false;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                isAnimtion = true;
                black_cover.startAnimation(alphaAnimation);
            }


        }

        class MyClick implements View.OnClickListener {
            private ViewHolder viewHolder;
            private View convertView;
            private int position;
            private VideoPlayView playView;


            public MyClick(ViewHolder viewHolder, View convertView, int position) {
                this.viewHolder = viewHolder;
                this.convertView = convertView;
                this.position = position;
                //这个palyview是总全局变量
                this.playView = viewHolder.play_view;
            }

            @Override
            public void onClick(View v) {
                isPlaying = true;
                currentPosition = position;
                currentItemView = convertView;
                //开始播放时隐藏图标
                viewHolder.play_btn.setVisibility(View.GONE);
                viewHolder.cover_image.setVisibility(View.GONE);

                playView.setUrl(videoPaths.get(position).getVideoUrl());

                setPlayView(playView);
                playView.openVideo();
                notifyDataSetChanged();
            }
        }
    }
    private BaiSi mainMessageParse = new BaiSi();
    int page = 1;
    //?userId=1111&pageSize=5&pageNum=1&token=123
    private void GetFriendsVideoFeedFromServer()
    {
        //http://api.budejie.com/api/api_open.php?a=list&c=data&maxtime=&type=41
        if(url==null){
            url = "http://api.budejie.com/api/api_open.php";
        }
        OkHttpUtils.get().url(url)
                .addParams("a","list")
                .addParams("c", "data")
                .addParams("maxtime", "")
                .addParams("type", "41")
                .build().execute(new Callback() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                String json = response.body().string();
                List<BaiSiBean> infos = mainMessageParse.getInfoList(json);
                for(int i=0;i<infos.size();i++){
                    BaiSiBean b = infos.get(i);
                    maxtime = b.getMaxtime();
                    //System.out.println("b.getRadio:"+b.getRadio()+"");
                    VideoInfo videoInfo = new VideoInfo(
                            b.getVideourl(), b.getImage(),
                            b.getText(),"",b.getRadio(),null);
                    path.add(videoInfo);
                }

                handler.sendEmptyMessage(0);
                return null;
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(Object response, int id) {

            }
        });



    }

    //加载更多
    boolean loading = false;
    private void LoadMore()
    {
        if(loading){
            return;
        }
        loading = true;

        OkHttpUtils.get().url(url)
                .addParams("a","list")
                .addParams("c", "data")
                .addParams("maxtime", maxtime)
                .addParams("type", "41")
                .build().execute(new Callback() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                String json = response.body().string();
                List<BaiSiBean> infos = mainMessageParse.getInfoList(json);

                for(int i=0;i<infos.size();i++){
                    BaiSiBean b = infos.get(i);
                    //System.out.println("b.getRadio:"+b.getRadio()+"");
                    VideoInfo videoInfo = new VideoInfo(
                            b.getVideourl(), b.getImage(),
                            b.getText(),"",b.getRadio(),null);
                    path.add(videoInfo);
                    maxtime = b.getMaxtime();
                }

                handler.sendEmptyMessage(0);
                return null;
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(Object response, int id) {

            }
        });




    }
}



