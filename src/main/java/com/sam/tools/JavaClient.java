package com.sam.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Logger;
//https://blog.csdn.net/yexuejianghan/article/details/119255663
public class JavaClient {

    public JavaClient() {
    }


    public  static Object remoteCall(String msg) {
        String HOST = "localhost";
        Integer PORT = 8888;
        Logger log = Logger.getLogger(JavaClient.class.getName());

        System.out.println(msg);
        // 访问服务进程的套接字
        Socket socket = null;
        // List<Question> questions = new ArrayList<>();
        log.info("调用远程接口:host=>" + HOST + ",port=>" + PORT);
        try {
            // 初始化套接字，设置访问服务的主机和进程端口号，HOST是访问python进程的主机名称，可以是IP地址或者域名，PORT是python进程绑定的端口号
            socket = new Socket(HOST, PORT);
            // 获取输出流对象
            OutputStream os = socket.getOutputStream();
            PrintStream out = new PrintStream(os);

//            // 获取服务进程的输入流
//            InputStream is = socket.getInputStream();
//            BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));

//            Scanner scanner = new Scanner(System.in);
            System.out.print("please input:");
//            String str = scanner.next();

            // 发送内容
            out.print(msg);
            // 告诉服务进程，内容发送完毕，可以开始处理
            out.print("over!!!");
            out.flush();


            String tmp = null;
            StringBuilder sb = new StringBuilder();
//                // 读取内容
//                while ((tmp = br.readLine()) != null) {
//                    sb.append(tmp).append('\n');
//                    break;
//                }

            // 解析结果
            System.out.println("接收服务端数据：" + (sb.toString()));
            System.out.print("please input:");

//            br.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            log.info("远程接口调用结束");
        }
        return msg;
    }


    public static void main(String[] args) {
        remoteCall("启动程序。。。");
    }
}

