package algorithm.nowcoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * HJ31 单词倒排
 * https://www.nowcoder.com/practice/81544a4989df4109b33c2d65037c5836?tpId=37&tqId=38366&rp=1&ru=/ta/huawei&qru=/ta/huawei&difficulty=2&judgeStatus=&tags=/question-ranking
 *
 * @author majun
 * @version 1.0
 * @since 2022-02-13 18:56
 */
public class MainHj31 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            int n = sc.nextInt();
            ArrayList<List<Integer>> lists = new ArrayList<>();
            for (int j = n; j >0; j--) {
                ArrayList<Integer> list = new ArrayList<>();
                for (int i = 1; i <= j; i++) {
                    Integer integer = list.size() == 0 ? 0 : list.get(list.size() - 1);
                    list.add(integer + i);


                }
                System.out.println(list.stream().map(integer -> integer + "").collect(Collectors.joining(" ")));
                lists.add(list);

            }

        }
    }
}
