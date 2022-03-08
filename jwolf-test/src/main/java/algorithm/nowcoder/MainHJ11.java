package algorithm.nowcoder;

import java.util.Scanner;

/**
 * HJ11 数字颠倒，类似题目，HJ12 字符串反转
 * https://www.nowcoder.com/practice/ae809795fca34687a48b172186e3dafe?tpId=37&tqId=21234&rp=1&ru=/ta/huawei&qru=/ta/huawei&difficulty=2&judgeStatus=&tags=/question-ranking
 *
 * @author majun
 * @version 1.0
 * @since 2022-02-13 17:56
 */
public class MainHJ11 {

    public static void main(String[] args) {
         Scanner sc = new Scanner(System.in);
         while (sc.hasNext()){
             System.out.println(new StringBuilder(sc.nextLine()).reverse().toString());
         }
    }
    /*public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            int i = sc.nextInt();
            if (i == 0) {
                System.out.print(i);
            }
            while (i > 0) {
                System.out.print(i % 10);
                i /= 10;

            }
        }

    }*/
}
