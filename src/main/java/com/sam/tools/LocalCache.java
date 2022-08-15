package com.sam.tools;

import java.io.*;

public class LocalCache<T extends Serializable> {

   public static void save(String key,Object obj) throws IOException {
       File file = new File("D:/" + key);

       if (file.exists()) {
           file.delete();
       }
       file.createNewFile();

       FileOutputStream fileOutputStream = new FileOutputStream(file);
       fileOutputStream.write(SerializeUtil.serialize(obj));
       fileOutputStream.close();

    }

   public static  <T extends Serializable>T getObject(String key,Class<T> clazz) throws IOException {
       File file = new File("D:/" + key);
       FileInputStream fileInputStream = new FileInputStream(file);
       byte[] bytes = new byte[33554432];
       fileInputStream.read(bytes);
       fileInputStream.close();
       return clazz.cast(SerializeUtil.unSerialize(bytes));
    }



    public static void main(String[] args) throws IOException {
 /*       BloomFilter stringBloomFilter = new BloomFilter(0.00001,10000000);
        for (int i = 0; i < 10000000; i++) {
           String s = "testtest" + i;
            stringBloomFilter.add(s);
        }
        System.out.println(stringBloomFilter.contains("asd"));
        save("stringBloomFilter", stringBloomFilter);
*/
        BloomFilter stringBloomFilter1 = getObject("stringBloomFilter", BloomFilter.class);
        System.out.println(stringBloomFilter1.contains("testtest1"));
        System.out.println(stringBloomFilter1.contains("testtest"));

    }


}
