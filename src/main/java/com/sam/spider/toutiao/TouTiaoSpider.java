package com.sam.spider.toutiao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sam.tools.HttpUtil;
import com.sam.tools.JavaClient;

import java.util.HashSet;
import java.util.Set;

public class TouTiaoSpider {
    public static void main(String[] args) throws InterruptedException {
        Set hashSet = new HashSet();

        while (true) {
            String url = "https://www.toutiao.com/hot-event/hot-board/?origin=toutiao_pc&_signature=_02B4Z6wo00f01u-b1XAAAIDCb5kvMPYYlYbvv9HAANkro6ZjSLItf.iCyaQQaWJHskVR8OsJhhVwuQ5pxZLw.-fDjOE0RbIIYO2g1hin4e6Fg6MjddKgT6M7p3Py1RTOc4l3A.Qdu620alzQ24";
            try {
                String html = HttpUtil.getHttpBody(url);
                JSONObject jsonObject = (JSONObject) JSON.parse(html);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                if (!jsonArray.isEmpty()) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        try{
                            JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                            String title = (String) jsonObject1.get("Title");
                            Boolean isExists = hashSet.contains(title);
                            if (!isExists) {
                                System.out.println(title);
                                JavaClient.remoteCall(title);
                                hashSet.add(title);
                                Thread.sleep(30000);//发一次微博等30秒
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Thread.sleep(1000*60*30);// 每30分钟爬一次
        }
    }
}
