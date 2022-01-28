package algorithm;

import java.math.BigInteger;
import java.util.Scanner;
/**
 * Description: 阶乘-递归
 *
 * @author majun
 * @version 1.0
 * @date 2022-01-29 01:11
 */
public class T020JiechengRecursion {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int i = scanner.nextInt();
            System.out.println(jiecheng(i).toString());
        }
    }

    private static BigInteger jiecheng(long i) {
        if (i == 1) {
            return BigInteger.ONE;
        } else {
            return BigInteger.valueOf(i).multiply(jiecheng(i - 1));
        }
    }
}