package com.ailin.algorithm;

import java.util.Arrays;
import java.util.Random;

/**
 * @author Administrator
 */
public class Demo01 {

    public static void main(String[] args) {

        int[] data = new int[15];
        for (int i = 0; i < data.length - 1; i++) {
            data[i] = new Random().nextInt(100);
        }
        data[data.length - 1] = 9;
        Arrays.sort(data);
        System.out.println(Arrays.toString(data));

        int dichotomy = dichotomy(data, 9);
        System.out.println("结果：" + dichotomy);
    }


    private static int dichotomy(int[] data, int b){

        if(data.length == 0){
            return -1;
        }

        int left = 0;
        int right = data.length;

        while (left < right){

            int mid = left + ((right-left) >> 1);

            if(data[mid] == b){
                return mid;
            }else if(data[mid] < b){
                //向左查
                left = mid+1;
            }else if(data[mid] > b){
                //向右查
                right = mid;
            }
        }

        return -1;
    }
}
