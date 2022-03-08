package algorithm.nowcoder;

import java.util.Scanner;

/**
 * HJ22 汽水瓶
 * https://www.nowcoder.com/practice/fe298c55694f4ed39e256170ff2c205f?tpId=37&tqId=21245&rp=1&ru=/ta/huawei&qru=/ta/huawei&difficulty=2&judgeStatus=&tags=/question-ranking
 *
 * @author majun
 * @version 1.0
 * @since 2022-02-13 17:56
 */
public class MainHj22 {

    public static void main(String[] args) {
         Scanner sc = new Scanner(System.in);
         while (sc.hasNext()){
             int i = sc.nextInt();
             if (i==0) {
                  break;
             }else {
                 System.out.println(i/2);
             }
         }
    }

}
