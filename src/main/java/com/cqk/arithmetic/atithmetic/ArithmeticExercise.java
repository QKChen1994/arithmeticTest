package com.cqk.arithmetic.atithmetic;

import com.cqk.arithmetic.atithmetic.service.ArithmeticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.text.DateFormatter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * @description: 算法练习
 * @author: Ed_Chen
 * @create: 2020/08/18 23:26
 **/
@Component
public class ArithmeticExercise {

    @Autowired
    private ArithmeticService arithmeticService;

    public static void main(String[] args) {
        getNotSevenNo();
    }

    /**
     * @Description: 给定一个数组，每次操作你可以选择数组中的任意一个数字将它改为任意值，请返回三次操作后nums中最大值
     * 和最小值的。
     * 例子：nums=[6,6,0,1,1,4,6]
     * 输出:2
     * @Params: [date]
     * @Return: void
     * @Author: Ed_Chen
     * @Date: 2020/8/19  20:34
     **/
    public int minDifference(int[] nums){
        /**
         * 只能改三次，肯定是把大的数改小了，或者把小的数改大了。
         * 先将数组排序，改三次不是在头就是在尾，用后四个减去前四个，
         * 求出最小的。那其他三个就是比较大的，改的时候改那三个就可以了
         **/
        Arrays.sort(nums);
        int n = nums.length;
        if(n<=4){
            return 0;
        }
        return Math.min(Math.min(nums[n-1]-nums[3],nums[n-2]-nums[2]),Math.min(nums[n-3]-nums[1],nums[n-4]-nums[0]));
    }
    
    /**
     * @Description: 数字从1开始，遇到数字7就会跳过，比如6后边直接是8，69后边直接是80，现在给你个数字，问是第几位？
     * 比如输入8，输出7，就是第7个数
     * @Return: int
     * @Author: Ed_Chen
     * @Date: 2020/8/20  23:30
     **/
    public static void getNotSevenNo(){
        Scanner scanner=new Scanner(System.in);
        System.out.println("请输入一个数字。。。。。。");
        if(scanner.hasNext()){
            String inputString=scanner.nextLine();
            if( inputString.contains("7")){
                System.out.println("请输入不含7的数字");
                return;
            }
            //正则判断输入是否为数字
            Pattern pattern = Pattern.compile("[0-9]*");
            if(!pattern.matcher(inputString).matches()){
                System.out.println("请输入数字");
                return;
            }

            int length = inputString.length();
            int sevenCount=0;
            for (int i = 0; i < length; i++) {
                char charnum = inputString.charAt(i);
                int num = Integer.parseInt(String.valueOf(charnum));
                //逐位计算，每位包含的7的数字的个数
                sevenCount += getNumOfPosition(num,length-i);
            }
            System.out.println("数字："+inputString+",包含7的数字的个数:"+sevenCount);
        }
        scanner.close();

    }
    /**
     * @Description:
     * @Params: [i(在此位上的数字), lehgth（位数，2代表十位）]
     * @Return: int
     * @Author: Ed_Chen
     * @Date: 2020/8/21  22:51
     **/
    public static int getNumOfPosition(int i,int lehgth){
        if(lehgth==1){
            return i<7?0:1;
        }
        int nextLength =lehgth-1;
        int nextnum=getNumOfPosition(10,nextLength);
        if(i<7){
            //小于7，计算i*下一位会有多少个包含7的数字
            return i * nextnum ;
        }else{
            int count= (int) ((i-1)*nextnum +Math.pow(10,(lehgth-1)));
            return count;
        }
    }

    /**
     * @Description: 输出匹配字符串的开始下标
     * 给出母串和子串，输出子串能够在母串完全匹配的开始位置
     * 例子： asdfasdfa，fas 输出3，就是最小下标
     * @Params: [inputStr：输入字符串, matchStr：匹配字符串]
     * @Return: void
     * @Author: Ed_Chen
     * @Date: 2020/8/25  23:26
     **/
    public void getIndexOfMatchString(String inputStr,String matchStr){

        int index= inputStr.indexOf(matchStr,0);
        System.out.println("下标："+index);
    }

    /**
     * @Description: 给定一个8位字符串的年月日格式的日期(yyyyMMdd)，计算日期为所在年份的第几天
     * @Params: [] 
     * @Return: void
     * @Author: Ed_Chen
     * @Date: 2020/9/15  13:11
     **/
    public long getDaysOfThisYear(String dayStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = sdf.parse(dayStr);
        Date fromDate = sdf.parse(dayStr.substring(0,4)+"0101");
        long cha = (date.getTime()-fromDate.getTime())/(24*60*60*1000)+1;
        return cha;
    }
}
