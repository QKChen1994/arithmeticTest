package com.cqk.arithmetic.designmode.singleton;

/**
 * @description: 懒汉模式（单例）
 * @author: Ed_Chen
 * @create: 2020/08/23 17:01
 **/
public class Idler {

    private static Idler idler ;

    private Idler(){

    }

    public static Idler getInstance(){
        if(idler==null){
            idler=new Idler();
        }
        return idler;
    }
}
