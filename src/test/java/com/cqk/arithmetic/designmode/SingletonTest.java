package com.cqk.arithmetic.designmode;

import com.cqk.arithmetic.designmode.singleton.EnumSingleton;
import com.cqk.arithmetic.designmode.singleton.HungryMan;
import com.cqk.arithmetic.designmode.singleton.Idler;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @description: 单例模式测试
 * @author: Ed_Chen
 * @create: 2020/08/23 17:08
 **/
@SpringBootTest
public class SingletonTest {
    
    /**
     * @Description: 懒汉模式
     * @Params: [] 
     * @Return: void
     * @Author: Ed_Chen
     * @Date: 2020/8/23  17:39
     **/
    @Test
    public void idlerTest(){
        Idler idler = Idler.getInstance();
    }

    /**
     * @Description: 饿汉模式
     * @Params: []
     * @Return: void
     * @Author: Ed_Chen
     * @Date: 2020/8/23  17:12
     **/
    @Test
    public void hungryManTest() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        HungryMan hungryMan1 = HungryMan.getInstance();
        HungryMan hungryMan2 = HungryMan.getInstance();
        System.out.println(hungryMan1==hungryMan2);
        //反射破解单例模式
        /*Class<HungryMan> clazz = (Class<HungryMan> )Class.forName("com.cqk.arithmetic.designmode.singleton.HungryMan");
        Constructor<HungryMan> hungryManConstructor= clazz.getDeclaredConstructor(null);
        hungryManConstructor.setAccessible(true);
        HungryMan hungryMan3=hungryManConstructor.newInstance();
        System.out.println(hungryMan3==hungryMan2);*/
    }

    /**
     * @Description: 枚举单例测试
     * @Params: [] 
     * @Return: void
     * @Author: Ed_Chen
     * @Date: 2020/8/23  20:07
     **/
    @Test
    public void enumTest(){
        EnumSingleton enumSingleton1=EnumSingleton.ENUM_SINGLETON;
        EnumSingleton enumSingleton2=EnumSingleton.ENUM_SINGLETON;
        EnumSingleton enumSingleton3=EnumSingleton.ENUM_SINGLETON2;
        System.out.println(enumSingleton1==enumSingleton2);
        System.out.println(enumSingleton2==enumSingleton3);
    }
}
