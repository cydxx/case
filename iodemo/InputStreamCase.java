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
        
        


    }
}
