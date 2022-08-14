package com.sam.spider.weibo;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Weibo {
    public static void main(String[] args) {
        // 1.生成httpclient，相当于该打开一个浏览器
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        // 2.创建get请求，相当于在浏览器地址栏输入 网址
        HttpGet request = new HttpGet("https://s.weibo.com/top/summary?cate=realtimehot");
        // 设置请求头，将爬虫伪装成浏览器
        request.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
//        如果有ip代理，可以加上如下代码
//        HttpHost proxy = new HttpHost(IP地址, 端口号);
//        RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
//        request.setConfig(config);
        try {
            // 3.执行get请求，相当于在输入地址栏后敲回车键
            response = httpClient.execute(request);

            // 4.判断响应状态为200，进行处理
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 5.获取响应内容
                HttpEntity httpEntity = response.getEntity();
                String html = EntityUtils.toString(httpEntity, "utf-8");
                 System.out.println(html);

                /**
                 * 下面是Jsoup展现自我的平台
                 */
                // 6.Jsoup解析html
                Document document = Jsoup.parse(html);
                // 像js一样，通过标签获取title
                Element item = document.getElementsByTag("tbody").first();
                Elements items = item.getElementsByTag("tr");

                for(Element tmp : items) {
                    Element rankEle = tmp.getElementsByTag("td").first();
                    Elements textEle = tmp.select(".td-02").select("a");
                    Elements tagEle = tmp.select(".td-03");
                    if("".equals(rankEle.text()))
                        System.out.println(textEle.text() + " " + tagEle.text());
                    else System.out.println(rankEle.text() + " " + textEle.text() + " " + tagEle.text());
                    System.out.println(textEle.attr("herf"));
                }

            } else {
                // 如果返回状态不是200，比如404（页面不存在）等，根据情况做处理，这里略
                System.out.println("返回状态不是200");
                System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 6.关闭
            HttpClientUtils.closeQuietly(response);
            HttpClientUtils.closeQuietly(httpClient);
        }
    }
}
