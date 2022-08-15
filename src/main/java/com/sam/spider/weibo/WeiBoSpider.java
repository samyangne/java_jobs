package com.sam.spider.weibo;

import com.sam.tools.HttpUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WeiBoSpider {
    public static void main(String[] args) {

        String url = "https://s.weibo.com/top/summary?cate=realtimehot";
        //这里以我初中学校的网站做示范 
        try{
            String html = HttpUtil.getHttpBody(url);
            System.out.println(html);
            Document document = Jsoup.parse(html);
            //延迟三秒开始爬虫
            Elements divs = document.select(".HotTopic_tit_eS4fv");
            for (Element div :  divs) {
                String span = div.text();
                System.out.println(span);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
