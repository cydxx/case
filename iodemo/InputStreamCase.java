package com.example.demo.utils;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;

import java.io.*;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * @author chengyadong
 * @date 2020/12/4 上午11:21
 * @Description: inputStream转换为字符串
 */
public class InputStreamCase {

    public static void main(String[] args) throws Exception{
        File file = new File("/Users/chengyadong5/temp/hello.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        //使用 CharStreams (Guava)
        Scanner scanner = new Scanner(fileInputStream).useDelimiter("\\A");
        String s = scanner.hasNext() ? scanner.next() : "";
        System.out.println(s);
        System.out.println("=============================================");

        //使用 Scanner (JDK)
        String s1 = CharStreams.toString(new InputStreamReader(fileInputStream, Charsets.UTF_8));
        System.out.println(s1);

        //使用 Stream API (Java 8)，此解决方案将不同的换行符(如\r\n)转换为\n
        String collect = new BufferedReader(new InputStreamReader(fileInputStream)).lines().collect(Collectors.joining("\n"));
        System.out.println(collect);
        
        //parallel stream api java8,此解决方案将不同的换行符(如\r\n)转换为\n
        String collect1 = new BufferedReader(new InputStreamReader(fileInputStream)).lines().parallel().collect(Collectors.joining("\n"));
        System.out.println(collect1);
        
        //使用 InputStreamReader and StringBuilder (JDK)
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder stringBuilder = new StringBuilder();
        Reader in = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
        int charsRead;
        while ((charsRead = in.read(buffer,0,buffer.length))>0){
            stringBuilder.append(buffer,0,charsRead);
        }
        System.out.println(stringBuilder.toString());

        //使用 ByteArrayOutputStream and inputStream.read (JDK)
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fileInputStream.read(bytes)) != -1){
            result.write(bytes,0,length);
        }
        String ss = result.toString();
        System.out.println(ss);
        
        //使用 BufferedReader (JDK). 此解决方案将不同的换行符(如\n\r)转换为行。分隔符系统属性(例如，在Windows中为“\r\n”)。
        //line.separator 行分隔符(换行符) 那么其与‘\n’ 有什么区别呢。系统的环境变量，那么系统就有肯能有差别 一般的为Window 下和Unix下其所表示意义就会不同。
        //这样写的话，则剔除了平台无关性，写一次代码跑通在Linux上和Window上都能够运行。  
        String property = System.getProperty("line.separator");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
        StringBuilder stringBuilder = new StringBuilder();
        boolean f = false;
        for (String line;(line = bufferedReader.readLine()) != null;){
            stringBuilder.append(f? property:"").append(line);
            f = true;
        }
        String sss = stringBuilder.toString();
        System.out.println(sss);
        
        //使用 BufferedInputStream and ByteArrayOutputStream (JDK)
        BufferedInputStream bf = new BufferedInputStream(fileInputStream);
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        int re = bf.read();
        while (re != -1){
            bo.write((byte) re);
            re = bf.read();
        }
        String ssss = bo.toString();
        System.out.println(ssss);

    }
}
