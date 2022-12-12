package com.cqk.arithmetic.test;

import com.alibaba.fastjson.JSON;

import java.util.*;

/**
 * @description: 查找数组中的重复数据
 * @author: Ed_Chen
 * @create: 2022/03/03 23:37
 **/
public class Test {
    public static void main(String[] args) {
        int[] arrayA = {7,3,5};
        int[] arrayB = {3,7,3,8,7};
        List<Integer> result = findDub(arrayA,arrayB);
        System.out.println(JSON.toJSON(result));
    }

    public static List<Integer> findDub(int[] arrayA, int[] arrayB){
        Map<Integer,Integer> countMapA = new HashMap<>();
        for(int a : arrayA){
            countMapA.put(a,countMapA.getOrDefault(a,0)+1);
        }
        Map<Integer,Integer> countMapB = new HashMap<>();
        for(int b : arrayB){
            countMapB.put(b,countMapB.getOrDefault(b,0)+1);
        }

        Set<Integer> keySet;
        if(countMapA.size()>countMapB.size()){
            keySet = countMapB.keySet();
        }else{
            keySet = countMapA.keySet();
        }

        List<Integer> result = new ArrayList<>();
        for(Integer key : keySet){
            Integer appearCountA = countMapA.get(key);
            Integer appearCountB = countMapB.get(key);
            if (appearCountA != null && appearCountB!=null) {
                int maxAppearCount = Math.max(appearCountA,appearCountB);
                for(int j =0;j<maxAppearCount;j++){
                    result.add(key);
                }
            }
        }
        return result;
    }
}
