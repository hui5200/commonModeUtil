package com.ailin;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class lhTest {

    @Test
    public void redisTest(){
        List<String> list = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            list.add("" + i);
        }
        System.out.println("++++++++++++++++++++");
        list.forEach(System.out::print);
        System.out.println();
        System.out.println("++++++++++++++++++++");

//        Collections.shuffle(list);
        Collections.reverse(list);

        List<String> synchronizedList = Collections.synchronizedList(list);



        System.out.println("===================");
        list.forEach(System.out::print);
        System.out.println();
        System.out.println("===================");

        Object[] a = list.toArray();
    }

    @Test
    public void testMap(){

        Map<String,String> map = new ConcurrentHashMap<>();
        Map<String,String> hashMap = new HashMap<>();

        hashMap.put(null,null);

        map.put("test", "112");

        System.out.println(map.get("test"));

    }

    @Test
    public void UUIDTest(){

        UUID uuid = UUID.randomUUID();
        System.out.println(uuid.toString().replaceAll("-",""));
    }


    @Test
    public void test1(){

        String s = "32332";

        String s1 = null;

        String s3 = s1 + s;

        System.out.println(s3);
    }


}
