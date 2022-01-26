package algorithm;

import java.util.Arrays;

/**
 * 冒泡排序思路——每次遍历两两比较，大的往后交换，第一次比较完，最大的在最后，第二比较完，第二大再倒数第二。。。
 * {1,4,6,2,3,5}
 * {1,4,2,3,5,6}
 * {1,2,3,4,5,6}
 * {1,2,3,4,5,6}
 * {1,2,3,4,5,6}
 * f(n)=(n-1)+(n-2)+...+1=n(n-3)/2
 * 时间复杂度O(n^2)
 *
 * @author majun
 * @version 1.0
 * @date 2022-01-26 23:51
 */
public class T012BubbleSort {
    public static void main(String[] args) {
        int[] arr = {1, 4, 6, 2, 3, 5};
        int[] sort = bubbleSort(arr);
        Arrays.stream(sort).forEach(System.out::println);
    }

    private static int[] bubbleSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j + 1] < arr[j]) {
                    int tmp = arr[j + 1];
                    arr[j + 1] = arr[j];
                    arr[j] = tmp;
                }
            }
        }
        return arr;
    }
}
