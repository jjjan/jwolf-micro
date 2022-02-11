package algorithm.nowcoder;

import java.util.Scanner;

/**
 * HJ7 取近似值，大于5向上取整，小于5向下取整
 * https://www.nowcoder.com/practice/3ab09737afb645cc82c35d56a5ce802a?tpId=37&tqId=21230&rp=1&ru=/ta/huawei&qru=/ta/huawei&difficulty=1&judgeStatus=&tags=/question-ranking
 *
 * @author majun
 * @version 1.0
 * @since 2022-02-11 22:26
 */
public class MainH17 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            double v = sc.nextDouble();
            int x = ((int) (v * 10)) % 10;// 扩大10倍取模，大于5向上取整，小于5向下取整
            System.out.println(x > 5 ? (int) Math.ceil(v) : (int) Math.floor(v));
        }
    }
}
