package com.cqk.arithmetic.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 订单计价处理
 * @author: Ed_Chen
 * @create: 2022/03/03 23:45
 **/
public class OrderPriceHandler {

    /**
     * @Description: 开头 1_1： 饮品-奶茶； 1_2:饮品-咖啡； 2_1：小料-普通； 2_2:小料-奶盖
     * @Params:
     * @Return:
     * @Author: Ed_Chen
     * @Date: 2022/03/03 23:50
     **/
    public static ConcurrentHashMap<String,String> productTypePriceMap = new ConcurrentHashMap(){{
        put("1_1_1","{\"type\":\"1_1_1\",\"name\":\"椰果奶茶\",\"price\":10}");
        put("1_1_2","{\"type\":\"1_1_2\",\"name\":\"西米奶茶\",\"price\":10}");
        put("1_1_3","{\"type\":\"1_1_3\",\"name\":\"蜂蜜奶茶\",\"price\":12}");
        put("1_1_4","{\"type\":\"1_1_4\",\"name\":\"杏仁奶茶\",\"price\":14}");

        put("1_2_1","{\"type\":\"1_2_1\",\"name\":\"美式咖啡\",\"price\":11}");
        put("1_2_2","{\"type\":\"1_2_2\",\"name\":\"拿铁咖啡\",\"price\":12}");

        put("2_1_1","{\"type\":\"2_1_1\",\"name\":\"红豆\",\"price\":2}");
        put("2_1_2","{\"type\":\"2_1_2\",\"name\":\"芋圆\",\"price\":2}");

        put("2_2_1","{\"type\":\"2_2_1\",\"name\":\"奥利奥奶盖\",\"price\":4}");
        put("2_2_2","{\"type\":\"2_2_2\",\"name\":\"芝士奶盖\",\"price\":5}");
    }};

    public static void main(String[] args) {
        /**
         * 大体用一个工厂的方法处理，计价之类的功能
         * 没有拆分出来类有点多，内部类不能实例化，运行不起来
         **/
        /*String inputJson = "[{\"type\":\"1_1_1\",\"num\":2,\"smallTypeList\":[\"2_1_1\",\"2_1_2\"]}]";
        List<InputVO> inputVOList = JSONArray.parseArray(inputJson,OrderPriceHandler.InputVO.class);
        List<Product> productList = new ArrayList<>();
        OrderPriceHandler orderPriceHandler = new OrderPriceHandler();
        String message = orderPriceHandler.getCountInfoMessage(inputVOList,productList);
        System.out.println(message);*/
    }

    public String getCountInfoMessage(List<InputVO> inputVOList,List<Product> productList){
        Double countPrice = count(inputVOList,productList);
        Order order = new Order();
        order.setCountPrice(countPrice);
        order.setProductList(productList);
        return JSON.toJSONString(order);
    }
    /**
     * @Description:
     * @Params: [inputVOList]  [{"type":"1_1_1","num":2,"smallTypeList":["2_1_1","2_1_2"]}]
     * @Return: java.lang.Double
     * @Author: Ed_Chen
     * @Date: 2022/03/03 23:53
     **/
    public Double count(List<InputVO> inputVOList,List<Product> productList){
        Double result = new Double(-1);

        int drinkCount = inputVOList.stream().mapToInt(InputVO::getNum).sum();
        if(drinkCount>10){
            return result;
        }
        for(InputVO inputVO : inputVOList){
            if (productTypePriceMap.containsKey(inputVO.getType()) && inputVO.getType().startsWith("1_")){
                Drink drink = ProductFactory.getDrinkInstance(inputVO.getType());
                Double count = drink.count(inputVO.getNum(),inputVO.getSmallTypeList());
                if(count<0){
                    result = new Double(-1);
                    break;
                }
                result = Double.sum(result,count);
                productList.add(drink);
            }else{
                result = new Double(-1);
                break;
            }
        }
        return result;
    }

    public static class ProductFactory {
        public static Drink getDrinkInstance(String type){
            String productInfo = productTypePriceMap.get(type);
            Drink drink = null;
            if(type.startsWith("1_1")){
                drink = JSON.parseObject(productInfo,TeaOfMilk.class);
            }else if(type.startsWith("1_2")){
                drink = JSON.parseObject(productInfo,Coffee.class);
            }
            return drink;
        }

        public static SmallMaterial getSmallInstance(String type){
            String productInfo = productTypePriceMap.get(type);
            SmallMaterial smallMaterial = null;
            if(type.startsWith("2_1")){
                smallMaterial = JSON.parseObject(productInfo,SmallMaterialCommon.class);
            }else if(type.startsWith("2_2")){
                smallMaterial = JSON.parseObject(productInfo,SmallMaterialMilk.class);
            }
            return smallMaterial;
        }
    }

    /**
     * @Description: 饮料
     * @Params:
     * @Return:
     * @Author: Ed_Chen
     * @Date: 2022/03/04 00:07
     **/
    @Data
    public abstract class Drink extends Product {
        private List<SmallMaterial> smallMaterialList;

        public Double count(int num,List<String> smallTypeList){
            Set<String> smallTypeSet = new HashSet<>(smallTypeList);
            if(!check(smallTypeList,smallTypeSet)){
                return Double.valueOf(-1);
            }
            List<SmallMaterial> smallMaterialList = new ArrayList<>();
            Double count = new Double(0);
            for(String smallType : smallTypeSet){
                SmallMaterial smallMaterial = ProductFactory.getSmallInstance(smallType);
                smallMaterialList.add(smallMaterial);
                count = Double.sum(count,smallMaterial.getPrice());
            }
            this.setSmallMaterialList(smallMaterialList);
            return count;
        }

        public abstract Boolean check(List<String> smallTypeList, Set<String> smallTypeSet);
    }

    @Data
    public class TeaOfMilk extends Drink {
        public Boolean check(List<String> smallTypeList, Set<String> smallTypeSet){
            if(smallTypeSet.size() != smallTypeList.size()){
                return false;
            }
            long smallMilkCount = smallTypeSet.stream().filter(a->a.startsWith("2_2")).count();
            if(smallMilkCount>1){
                return false;
            }
            return true;
        }
    }

    @Data
    public class Coffee extends Drink {
        @Override
        public Boolean check(List<String> smallTypeList, Set<String> smallTypeSet) {
            if(smallTypeSet.size() != smallTypeList.size()){
                return false;
            }
            long smallMilkCount = smallTypeSet.stream().filter(a->a.startsWith("2_2")).count();
            if(smallMilkCount>1){
                return false;
            }
            long smallCommonCount = smallTypeSet.stream().filter(a->!a.startsWith("2_2")).count();
            if(smallCommonCount>0){
                return false;
            }
            return true;
        }
    }


    @Data
    @AllArgsConstructor
    public class InputVO {
        private String type;
        private int num;
        private List<String> smallTypeList;

        public InputVO() {
        }
    }

    @Data
    public abstract class Product{
        private String name;
        private Double price;
        private String type;
    }

    /**
     * @Description: 小料
     * @Params:
     * @Return:
     * @Author: Ed_Chen
     * @Date: 2022/03/04 00:13
     **/
    @Data
    public abstract class SmallMaterial extends Product {
        public abstract int check(String parentType);
    }

    /**
     * @Description: 普通小料
     * @Params:
     * @Return:
     * @Author: Ed_Chen
     * @Date: 2022/03/04 00:13
     **/
    @Data
    public class SmallMaterialCommon extends SmallMaterial {

        @Override
        public int check(String parentType) {
            return 0;
        }
    }

    /**
     * @Description: 奶盖
     * @Params:
     * @Return:
     * @Author: Ed_Chen
     * @Date: 2022/03/04 00:14
     **/
    @Data
    public abstract class SmallMaterialMilk extends SmallMaterial {
        @Override
        public int check(String parentType) {

            return 0;
        }
    }

    @Data
    public class Order{
        private Double countPrice;
        private List<Product> productList;
    }



}
