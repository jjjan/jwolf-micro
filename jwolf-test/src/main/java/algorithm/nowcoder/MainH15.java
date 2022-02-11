package algorithm.nowcoder;

import java.util.Scanner;

/**
 * HJ15 求int型正整数在内存中存储时1的个数
 * https://www.nowcoder.com/practice/440f16e490a0404786865e99c6ad91c9?tpId=37&tqId=21238&rp=1&ru=/ta/huawei&qru=/ta/huawei&difficulty=1&judgeStatus=&tags=/question-ranking
 * 法1.Integer api转为2进行后计数
 * 法2:位运算会快一点，x%2==1时x>>>1，计算器+1
 *
 * @author majun
 * @version 1.0
 * @since 2022-02-11 22:26
 */
public class MainH15 {

//    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//        while (sc.hasNext()) {
//            int x = sc.nextInt();
//            char[] chars = Integer.toBinaryString(x).toCharArray();
//            //Integer.toBinaryString(num).replaceAll("0", "").length()
//            int count = 0;
//            for (char c : chars){
//                if (c == '1') {
//                    count++;
//                }
//            }
//            System.out.println(count);
//        }
//    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int b = in.nextInt();
        int result = 0;
        while (b > 0) {
            if (1 == b % 2)
                result++;
            b = b >>> 1;
        }
        System.out.println(result);
    }
}
