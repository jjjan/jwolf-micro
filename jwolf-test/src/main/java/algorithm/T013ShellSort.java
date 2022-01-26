package algorithm;

import java.util.Arrays;

/**
 * 希尔排序：指定增量并逐步减少增量直到1时排序完成，增量步长越小排序越精确但更耗时
 * 排序结果是趋势递增的，固为不稳定排序方法
 *
 * @author majun
 * @version 1.0
 * @date 2022-01-26 23:51
 */
public class T013ShellSort {
    public static void main(String[] args) {
        int[] arr = {1, 4, 6, 2, 3, 5, 24, 2, 6};
        int[] sort = shellSort(arr);
        Arrays.stream(sort).forEach(System.out::println);
    }

    private static int[] shellSort(int[] arr) {
        int d = arr.length / 2;//自定义增量，常用数组长度一半
        while (d >= 1) {
            for (int i = 0; i + d < arr.length; i += d) {
                if (arr[i + d] < arr[i]) {
                    int tmp = arr[i + 1];
                    arr[i + 1] = arr[i];
                    arr[i] = tmp;
                }
            }
            d /= 2;//逐步缩小增量
        }
        return arr;
    }
}
