package com.duanqu.Idea.JsonParse;

import com.duanqu.Idea.bean.BaiSiBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Me on 2017/4/7.
 */

public class BaiSi {
    public List<BaiSiBean> getInfoList(String json)
    {
        LinkedList<BaiSiBean> infos = new LinkedList<>();
        try {
            JSONObject object = new JSONObject(json);
            JSONObject info = object.getJSONObject("info");
            JSONArray arrays = object.getJSONArray("list");
            for (int i=0;i<arrays.length();i++)
            {
                BaiSiBean baiSiBean = new BaiSiBean();
                JSONObject video = arrays.getJSONObject(i);
                baiSiBean.setMaxtime(info.getString("maxtime"));
                baiSiBean.setVideourl(video.getString("videouri"));
                baiSiBean.setHeadurl(video.getString("profile_image"));
                baiSiBean.setImage(video.getString("bimageuri"));
                baiSiBean.setName(video.getString("name"));
                baiSiBean.setText(video.getString("text"));
                baiSiBean.setWidth(video.getString("width"));
                baiSiBean.setHeight(video.getString("height"));
                int width = Integer.parseInt(baiSiBean.getWidth());
                int height = Integer.parseInt(baiSiBean.getHeight());

                float radio = width/(float)height;

                int tag=2;
                if(radio<1.4){
                    tag = 1;
                }
                baiSiBean.setRadio(tag);
                infos.add(baiSiBean);
                System.out.println(baiSiBean.getRadio()+"radio");
                System.out.println(baiSiBean.toString());
            }
            return infos;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


}
