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
        shellSort(arr);
        System.out.println(Arrays.toString(arr));
    }

    private static void shellSort(int[] arr) {
        int gap= arr.length / 2;//自定义增量，常用数组长度一半
        while (gap >= 1) {
           for (int i = gap; i < arr.length; i++) {
               int tmp=arr[i];
               int j;//找到待插入的坑位j
               for (j = i; j >= gap && tmp<arr[j - gap]; j -= gap) {
                    arr[j] = arr[j - gap];//后移 ,每次移动步长为gap
                }
                arr[j] = tmp;//在j坑位插入tmp

           }

            gap /= 2;//逐步缩小增量，最后一次gap=1相当于进行一次简单插入排序
        }
    }

}
