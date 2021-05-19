package com.ailin.dataStructure;

import java.util.Arrays;

public class Sorting {

    public static void main(String[] args) {

        int[] data = new int[25];
        for (int i = 0; i < 25; i++) {
            data[i] = (int) (Math.random() * 100);
        }
        System.out.println(Arrays.toString(data));

        //插入排序
//        insertSorting(data);

        //希尔排序
        shellSorting(data);
    }

    //插入排序
    private static void insertSorting(int[] data){

        for (int i = 0; i < data.length; i++) {
            for (int j = i; j > 0; j--) {
                if(data[j-1] > data[j]){
                    swap(data,j,j-1);
                }
            }
        }
        System.out.println(Arrays.toString(data));
    }

    //希尔排序
    private static void shellSorting(int[] data){

        int len = data.length/2;
        for (int i = 0; i < data.length/2; i++) {
            for (int j = 0; j < data.length/2; j++) {
                if(data[j] < data[len-1]){
                    swap(data,j,len-1);
                }
            }
            len = len/2;
        }
        System.out.println(Arrays.toString(data));
    }

    private static void swap(int[] data, int a, int b){
        int temp = data[a];
        data[a] = data[b];
        data[b] = temp;
    }
}
