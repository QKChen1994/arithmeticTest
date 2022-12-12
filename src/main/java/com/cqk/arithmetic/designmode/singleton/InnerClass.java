package com.cqk.arithmetic.designmode.singleton;

/**
 * @description: 内部嵌套类
 * @author: Ed_Chen
 * @create: 2020/08/23 17:39
 **/
public class InnerClass {

    private InnerClass(){
        if(InnerClassHolder.INNER_CLASS!=null){
            throw new RuntimeException();
        }
    }

    private static class InnerClassHolder{
        private static final InnerClass INNER_CLASS = new InnerClass();
    }

    public static InnerClass getInstance(){
        return InnerClassHolder.INNER_CLASS;
    }
}
