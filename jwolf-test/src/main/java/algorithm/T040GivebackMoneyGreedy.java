package algorithm;


/**
 * Description: 找零钱——贪心，注意：贪心算法得到的可能是局部最优解，如下的01背包问题
 * https://blog.csdn.net/qq_37763204/article/details/79289532?spm=1001.2101.3001.6650.2&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-2.pc_relevant_default&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-2.pc_relevant_default&utm_relevant_index=3
 *
 * @author majun
 * @version 1.0
 * @date 2022-01-29 01:11
 */
public class T040GivebackMoneyGreedy {
    public static void main(String[] args) {
        int[] m = {25, 10, 5, 1};
        int target = 99;
        int[] results = giveMoney(m, target);
        System.out.println(target + "的找钱方案:");
        for (int i = 0; i < results.length; i++) {
            System.out.println(results[i] + "枚" + m[i] + "面值");
        }
    }


    public static int[] giveMoney(int[] m, int target) {
        int k = m.length;
        int[] num = new int[k];
        for (int i = 0; i < k; i++) {
            num[i] = target / m[i];
            target = target % m[i];
        }
        return num;
    }
}
