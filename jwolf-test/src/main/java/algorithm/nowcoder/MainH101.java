package algorithm.nowcoder;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * HJ101 输入整型数组和排序标识，对其元素按照升序或降序进行
 * https://www.nowcoder.com/practice/dd0c6b26c9e541f5b935047ff4156309?tpId=37&tqId=21324&rp=1&ru=/ta/huawei&qru=/ta/huawei&difficulty=1&judgeStatus=&tags=/question-ranking
 *
 * @author majun
 * @version 1.0
 * @since 2022-02-11 22:26
 */
public class MainH101 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            int i = Integer.parseInt(sc.nextLine());
            String[] split = sc.nextLine().split(" +");
            if (split.length != i) {
                throw new RuntimeException("输入有误");
            }
            int flag = Integer.parseInt(sc.nextLine());
            String s;
            if (flag == 0) {
                s = Arrays.stream(split).sorted((s1, s2) -> Integer.parseInt(s1) - Integer.parseInt(s2)).collect(Collectors.joining(" "));

            } else if (flag == 1) {
                s = Arrays.stream(split).sorted((s1, s2) -> Integer.parseInt(s2) - Integer.parseInt(s1)).collect(Collectors.joining(" "));

            } else {
                throw new RuntimeException("输入有误");
            }
            System.out.println(s);
        }
    }
}
