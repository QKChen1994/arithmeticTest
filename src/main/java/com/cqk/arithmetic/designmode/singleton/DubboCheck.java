package com.cqk.arithmetic.designmode.singleton;

/**
 * @description: 双重检验锁
 * @author: Ed_Chen
 * @create: 2020/08/23 17:40
 **/
public class DubboCheck {
    private static DubboCheck dubboCheck;

    private DubboCheck() {
       if(dubboCheck!=null){
           throw new RuntimeException();
       }
    }

    public static DubboCheck getInstance(){
        if(dubboCheck==null){
            synchronized (DubboCheck.class){
                if(dubboCheck==null){
                    dubboCheck=new DubboCheck();
                }
            }
        }
        return dubboCheck;
    }
}
