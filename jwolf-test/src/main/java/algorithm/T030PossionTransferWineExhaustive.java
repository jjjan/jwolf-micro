package algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Description: 泊松分酒-穷举法
 * 版本1（无限制版本，即该案例）：https://www.cnblogs.com/Marcusyang/p/13011581.html
 * 版本2：https://blog.csdn.net/update_java/article/details/46051493
 * @author majun
 * @version 1.0
 * @date 2022-01-29 01:11
 */
public class T030PossionTransferWineExhaustive {

    public static List<String> containersUsedStr = new ArrayList<>(); //容器使用，如果已有相应串，说明不用再尝试该情形咯
    public static int[] maxN = new int[3];
    public static int[] N = new int[3];
    public static int target;
    public static int count = 0;

    //idx1倒入idx2
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
            if (!containersUsedStr.contains(str)) {
                containersUsedStr.add(str);
                count++;
            } else {
                N[idx1] = temp1;
                N[idx2] = temp2;
            }
        }
    }

    
    public boolean isContainTarget() {
        boolean anyMatch = Arrays.stream(N).anyMatch(i -> i == target);
        if (anyMatch) {
            containersUsedStr.stream().forEach(System.out::println);
        }
        return anyMatch;
    }

    public void getResult() {
        int max = Math.max(maxN[0], Math.max(maxN[1], maxN[2]));
        if (target > max) {
            System.out.println("不可能");
            return;
        }
        while (true) {
            int temp = count;
            //A瓶向B瓶倒
            transfer(0, 1);
            if (isContainTarget())
                break;
            //B瓶向C瓶倒
            transfer(1, 2);
            if (isContainTarget())
                break;
            //C瓶向A瓶倒
            transfer(2, 0);
            if (isContainTarget())
                break;
            //A瓶向C瓶倒
            transfer(0, 2);
            if (isContainTarget())
                break;
            //C瓶向B瓶倒
            transfer(2, 1);
            if (isContainTarget())
                break;
            //B瓶向A瓶倒
            transfer(1, 0);
            if (isContainTarget())
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
        target = Integer.valueOf(arrayS[6]);
        String str = String.format("%s,%s,%s", N[0], N[1], N[2]);
        containersUsedStr.add(str);
        test.getResult();
    }
}
