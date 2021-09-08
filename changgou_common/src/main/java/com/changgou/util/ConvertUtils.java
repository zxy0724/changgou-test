package com.changgou.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ConvertUtils {
    /**
     * 输入流转换为xml字符串
     * @param inputStream
     * @return
     */
    public static String convertToString(InputStream inputStream)throws IOException{
        ByteArrayOutputStream outStream= new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len =0;
        while((len=inputStream.read(buffer))!=-1){
            outStream.write(buffer,0,len);
        }
        outStream.close();
        inputStream.close();
        String result = new String(outStream.toByteArray(),"utf-8");
        return result;
    }
}
