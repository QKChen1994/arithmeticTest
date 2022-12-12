package com.cqk.arithmetic.designmode.singleton;

import java.io.Serializable;

/**
 * @description: 饿汉模式
 * @author: Ed_Chen
 * @create: 2020/08/23 17:10
 **/
public class HungryMan implements Serializable {
    private static HungryMan hungryMan = new HungryMan();

    private HungryMan(){
        //当他人使用反射破解单例模式时，若已实例化，抛出异常
        if(hungryMan!=null){
            throw new RuntimeException();
        }
    }

    public static HungryMan getInstance(){
        return hungryMan;
    }

    /**
     * @Description: 继承Serializable后，防止序列化，破解单例模式，需要实现readResolve方法
     * @Params: [] 
     * @Return: java.lang.Object
     * @Author: Ed_Chen
     * @Date: 2020/8/23  17:41
     **/
    public Object readResolve(){
        return hungryMan;
    }
}
