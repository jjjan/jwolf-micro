package algorithm;

import java.util.Arrays;

/**
 * 插入排序与二分插入排序——遍历数组，如果比它前一位小就往前插入，坑位后的元素后移一位
 * {1,4,6,2,3,5}
 * {1,4,6,2,3,5}
 * {1,2,4,6,3,5}将2插入1后4前
 * {1,2,3,4,6,5}bubble
 * {1,2,3,4,5,6}
 * 时间复杂度O(n^2)
 *
 * @author majun
 * @version 1.0
 * @date 2022-01-26 23:51
 */
public class T011InsertSort {
    public static void main(String[] args) {
        int[] arr = {1, 6, 2, 4, 3, 5};
        //int[] sort = insertSort(arr);
        int[] sort = insertSortWithBinarySearch(arr);
        Arrays.stream(sort).forEach(System.out::println);
    }

    private static int[] insertSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int tmp = arr[i];
            int j;
            for (j = i - 1; j >= 0; j--) {
                if (tmp < arr[j]) {
                    arr[j + 1] = arr[j]; //挪出一坑位,这里是对前部分已排好序的子序列遍历查找坑位，有优化为二分查找
                } else {
                    break;
                }
            }
            arr[j + 1] = tmp; //在坑位插入tmp
        }
        return arr;
    }


    /**
     * 查找插入坑位的过程改为二分查找
     *
     * @param arr
     * @return
     */
    private static int[] insertSortWithBinarySearch(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int tmp = arr[i];
            int left = 0;
            int right = i - 1;
            while (left <= right) {
                int mid = (left + right) / 2;
                if (tmp > arr[mid]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            //left位置即为坑位，其后的元素后挪一个位置
            for (int j = i - 1; j >= left; j--) {
                arr[j + 1] = arr[j];
            }
            //插入当前遍历的元素
            if (left != i) {
                arr[left] = tmp;
            }

        }
        return arr;
    }
}
