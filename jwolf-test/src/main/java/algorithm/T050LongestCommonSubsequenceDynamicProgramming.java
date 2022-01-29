package algorithm;


import java.util.Scanner;

/**
 * 最长公共子串
 * https://github.com/labuladong/fucking-algorithm/blob/master/%E5%8A%A8%E6%80%81%E8%A7%84%E5%88%92%E7%B3%BB%E5%88%97/%E6%9C%80%E9%95%BF%E5%85%AC%E5%85%B1%E5%AD%90%E5%BA%8F%E5%88%97.md * @author majun
 *
 * @version 1.0
 * @since 2022-01-29 23:11
 */
public class T050LongestCommonSubsequenceDynamicProgramming {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] split = line.split(" ");
            int len = longestCommonSubsequence(split[0], split[1]);
            System.out.println(len);

        }


    }

    public static int longestCommonSubsequence(String text1, String text2) {
        // 字符串转为char数组以加快访问速度
        char[] str1 = text1.toCharArray();
        char[] str2 = text2.toCharArray();

        int m = str1.length, n = str2.length;
        // 构建dp table，初始值默认为0(不一定非要增加1列全0首列及1列全0首行)
        int[][] dp = new int[m + 1][n + 1];
        // 状态转移
        for (int i = 1; i <= m; i++)
            for (int j = 1; j <= n; j++)
                if (str1[i - 1] == str2[j - 1])
                    // 找到LCS中的字符
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                else
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);

        return dp[m][n];
    }


}
