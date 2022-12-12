package com.cqk.arithmetic;

import com.cqk.arithmetic.atithmetic.ArithmeticExercise;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;

@SpringBootTest
class ArithmeticApplicationTests {

    @Autowired
    private ArithmeticExercise arithmeticExercise;

    @Test
    void minDifferenceTest() {
        int[] nums = new int[]{6, 6, 0, 1, 1, 4, 6};
        int i =arithmeticExercise.minDifference(nums);
    }
    /**
     * @Description: 数字从1开始，遇到数字7就会跳过，比如6后边直接是8，69后边直接是80，现在给你个数字，问是第几位？
     * @Params: []
     * @Return: void
     * @Author: Ed_Chen
     * @Date: 2020/8/25  23:36
     **/
   /* @Test
    void notSevenTest2() {
        arithmeticExercise.getNotSevenNo();
    }*/


    @Test
    void getIndexOfMatchStringTest() {
        arithmeticExercise.getIndexOfMatchString("a89525sdfghjklasdf","df");
    }

    @Test
    public void getDaysOfThisYearTest() throws ParseException {
        long cha =arithmeticExercise.getDaysOfThisYear("20200302");
        System.out.println("为第"+cha+"天");
    }

    @Test
    public void get() throws ParseException {
        geta("abcdacd");
    }

    public void geta(String s){
        int[] last = new int[128];
//        for(int i = 0; i < 128; i++) {
//            last[i] = -1;
//        }
        int n = s.length();

        int res = 0;
        int start = 0; // 窗口开始位置
        for(int i = 0; i < n; i++) {
            int index = s.charAt(i);
            start = Math.max(start, last[index]);
            res   = Math.max(res, i - start + 1);
            last[index] = i+1;
        }
        System.out.println("res:"+res);
    }
}
