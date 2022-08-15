package com.sam.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CompressUtil {

    public static byte[] compress(byte[] bt){
        //将byte数据读入文件流
        ByteArrayOutputStream bos=null;
        GZIPOutputStream gzipos=null;
        try {
            bos=new ByteArrayOutputStream();
            gzipos=new GZIPOutputStream(bos);
            gzipos.write(bt);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            closeStream(gzipos);
            closeStream(bos);
        }
        return bos.toByteArray();
    }


    public static byte[] unCompress(byte[] bt){
        ByteArrayOutputStream byteAos=null;
        ByteArrayInputStream byteArrayIn=null;
        GZIPInputStream gzipIn=null;
        try {
            byteArrayIn=new ByteArrayInputStream(bt);
            gzipIn=new GZIPInputStream(byteArrayIn);
            byteAos=new ByteArrayOutputStream();
            byte[] b=new byte[4096];
            int temp = -1;
            while((temp=gzipIn.read(b))>0){
                byteAos.write(b, 0, temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally{
            closeStream(byteAos);
            closeStream(gzipIn);
            closeStream(byteArrayIn);
        }
        return byteAos.toByteArray();
    }


    public static void closeStream(Closeable oStream){
        if(null!=oStream){
            try {
                oStream.close();
            } catch (IOException e) {
                oStream=null;//赋值为null,等待垃圾回收
                e.printStackTrace();
            }
        }
    }


}
