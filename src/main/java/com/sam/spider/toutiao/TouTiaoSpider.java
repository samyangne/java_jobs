package com.sam.spider.toutiao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sam.tools.*;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class TouTiaoSpider {
    public static void main(String[] args) throws InterruptedException {

        BloomFilter stringBloomFilter = null;
        String bloomName = "TouTiaoSpider";

        try{
            stringBloomFilter = LocalCache.getObject(bloomName, BloomFilter.class);
        }catch (Exception e){
            stringBloomFilter = new BloomFilter(0.00001,10000000);
        }

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
                            Boolean isExists = stringBloomFilter.contains(title);
                            if (!StringUtils.isEmpty(title) && !KeyWordUtil.keyWordSet.contains(title) && !isExists) {
                                System.out.println("准备发送："+title);
                                stringBloomFilter.add(title);
                                LocalCache.save(bloomName,stringBloomFilter);
                                JavaClient.remoteCall(title);
                                Thread.sleep(30000);//发一次微博等30秒
                            } else {
                                System.out.println("已发送过："+title);
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Thread.sleep(1000*60*10);// 每30分钟爬一次
        }
    }
}
