package com.cqk.arithmetic.atithmetic.service;

import org.springframework.stereotype.Component;

/**
 * @description: 算法service
 * @author: Ed_Chen
 * @create: 2020/08/25 22:50
 **/
@Component
public class ArithmeticService {
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


}
