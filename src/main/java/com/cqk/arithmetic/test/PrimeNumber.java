package com.cqk.arithmetic.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 质数
 * @author: Ed_Chen
 * @create: 2022/03/09 22:16
 **/
public class PrimeNumber {

    public static void main(String[] args) {
        boolean result = isPrime(3);
        System.out.println(result);

        StringConvert("abcvfg");

        File file = new File("t.ext.img");
        checkFile(file);
    }

    /**
     * @Description: 质数
     * @Params: [a] 
     * @Return: boolean
     * @Author: Ed_Chen
     * @Date: 2022/3/9  22:40
     **/
    public static boolean isPrime(int a) {
        boolean flag = true;
        if(a<2){
            flag = false;
        }else{
            int num = (int) Math.sqrt(a);
            for(int i = 2;i<=num;i++){
                if (a % i == 0) {
                    flag = false;
                }
            }
        }
        return flag;
    }

    
    /**
     * @Description: 字符串反转
     * @Params: [s] 
     * @Return: void
     * @Author: Ed_Chen
     * @Date: 2022/3/9  22:41
     **/
    public static void StringConvert(String s){
        for(int i= s.length()-1;i>=0;i--){
            System.out.println(s.charAt(i));
        }
    }

    public static List<String> allowFile = new ArrayList(){{
        add("jpg");
        add("png");
        add("pdf");
        add("doc");
        add("mp4");
        add("mov");
        add("mp3");
    }};
    /**
     * @Description: 检查文件后缀
     * @Params: [file] 
     * @Return: void
     * @Author: Ed_Chen
     * @Date: 2022/3/9  23:00
     **/
    public static void checkFile(File file){
        String fileName = file.getName();
        String[] fileSuffixArray = fileName.split("\\.");
        String fileSuffix = fileSuffixArray[fileSuffixArray.length-1];
        if(allowFile.contains(fileSuffix)){
            System.out.println("允许");
        }else{
            System.out.println("不允许");
        }
    }
    
}
