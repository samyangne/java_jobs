package com.sam.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**  
 * @功能描述: 序列化对象工具
 */
public class SerializeUtil {

    /**  
     * @功能描述: 序列化对象
     */ 
    public static byte[] serialize(Object object) {
        try {
            // 序列化
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();

            // 压缩数组
            bytes = CompressUtil.compress(bytes);

            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**  
     * @功能描述: 反序列化对象
     */ 
    public static Object unSerialize( byte[] bytes) {
        ByteArrayInputStream bais = null;
        try {
            // 解压缩
            bytes = CompressUtil.unCompress(bytes);
            // 反序列化
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void main(String [] args) {
    }
}
