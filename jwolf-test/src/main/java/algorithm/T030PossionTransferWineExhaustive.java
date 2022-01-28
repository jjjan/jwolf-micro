package algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Description: 泊松分酒-穷举法
 *
 * @author majun
 * @version 1.0
 * @date 2022-01-29 01:11
 */
public class T030PossionTransferWineExhaustive {

    public static List<String> set = new ArrayList<>();
    public static int[] maxN = new int[3];
    public static int[] N = new int[3];
    public static int ans;
    public static int count = 0;

    //某from瓶向end瓶中倒
    public void transfer(int idx1, int idx2) {
        if (N[idx1] == 0)
            return;
        if (maxN[idx2] > N[idx2]) {
            int low = maxN[idx2] - N[idx2];
            int temp1 = N[idx1], temp2 = N[idx2];
            if (low >= N[idx1]) {
                N[idx2] = N[idx2] + N[idx1];
                N[idx1] = 0;
            } else {
                N[idx2] = maxN[idx2];
                N[idx1] = N[idx1] - low;
            }
            String str = String.format("%s,%s,%s", N[0], N[1], N[2]);
            if (!set.contains(str)) {
                set.add(str);
                count++;
            } else {
                N[idx1] = temp1;
                N[idx2] = temp2;
            }
        }
    }

    public boolean check() {
        boolean anyMatch = Arrays.stream(N).anyMatch(i -> i == ans);
        if (anyMatch) {
            set.stream().forEach(System.out::println);
        }
        return anyMatch;
    }

    public void getResult() {
        int max = Math.max(maxN[0], Math.max(maxN[1], maxN[2]));
        if (ans > max) {
            System.out.println("不可能");
            return;
        }
        while (true) {
            int temp = count;
            //A瓶向B瓶倒
            transfer(0, 1);
            if (check())
                break;
            //B瓶向C瓶倒
            transfer(1, 2);
            if (check())
                break;
            //C瓶向A瓶倒
            transfer(2, 0);
            if (check())
                break;
            //A瓶向C瓶倒
            transfer(0, 2);
            if (check())
                break;
            //C瓶向B瓶倒
            transfer(2, 1);
            if (check())
                break;
            //B瓶向A瓶倒
            transfer(1, 0);
            if (check())
                break;
            temp = count - temp;
            if (temp == 0) {
                System.out.println("不可能");
                return;
            }
        }
    }

    public static void main(String[] args) {
        T030PossionTransferWineExhaustive test = new T030PossionTransferWineExhaustive();
        Scanner in = new Scanner(System.in);
        String S = in.next();
        String[] arrayS = S.split(",");
        for (int i = 0; i < 3; i++)
            maxN[i] = Integer.valueOf(arrayS[i]);
        for (int i = 3; i < 6; i++)
            N[i - 3] = Integer.valueOf(arrayS[i]);
        ans = Integer.valueOf(arrayS[6]);
        String str = String.format("%s,%s,%s", N[0], N[1], N[2]);
        set.add(str);
        test.getResult();
    }
}
