package algorithm;

import java.util.Arrays;

/**
 * 选择排序思路——遍历数组，依次找到最小的排序
 * {1,4,6,2,3,5}
 * 1{4,6,2,3,5}
 * 1,2{4,6,3,5}
 * 1,2,3{4,6,5}
 * f(n)=n+(n-1)+(n-2)+...+1=n(n-1)/2
 * 时间复杂度O(n^2)
 *
 * @author majun
 * @version 1.0
 * @date 2022-01-25 23:51
 */
public class T010SelectSort {
    public static void main(String[] args) {
        int[] arr = {1, 4, 6, 2, 3, 5};
        int[] sort = selectSort(arr);
        Arrays.stream(sort).forEach(System.out::println);
    }

    private static int[] selectSort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int min = arr[i];
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < min) {
                    int tmp = arr[j];
                    arr[j] = min;
                    min = tmp;
                }
            }
            arr[i] = min;
        }
        return arr;
    }
}
