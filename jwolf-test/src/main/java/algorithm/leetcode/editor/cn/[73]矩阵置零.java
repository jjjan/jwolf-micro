package algorithm.leetcode.editor.cn;//给定一个 m x n 的矩阵，如果一个元素为 0 ，则将其所在行和列的所有元素都设为 0 。请使用 原地 算法。
//
// 
// 
//
// 
//
// 示例 1： 
//
// 
//输入：matrix = [[1,1,1],[1,0,1],[1,1,1]]
//输出：[[1,0,1],[0,0,0],[1,0,1]]
// 
//
// 示例 2： 
//
// 
//输入：matrix = [[0,1,2,0],[3,4,5,2],[1,3,1,5]]
//输出：[[0,0,0,0],[0,4,5,0],[0,3,1,0]]
// 
//
// 
//
// 提示： 
//
// 
// m == matrix.length 
// n == matrix[0].length 
// 1 <= m, n <= 200 
// -231 <= matrix[i][j] <= 231 - 1 
// 
//
// 
//
// 进阶： 
//
// 
// 一个直观的解决方案是使用 O(mn) 的额外空间，但这并不是一个好的解决方案。 
// 一个简单的改进方案是使用 O(m + n) 的额外空间，但这仍然不是最好的解决方案。 
// 你能想出一个仅使用常量空间的解决方案吗？ 
// 
// Related Topics 数组 哈希表 矩阵 
// 👍 692 👎 0

import java.util.TreeSet;

/*
 * 111   101
 * 101   000
 * 123   101
 *
 *
 * */
//leetcode submit region begin(Prohibit modification and deletion)
class Solution73 {
    public void setZeroes(int[][] matrix) {
        //优化： 把集合改为标记数组，避免重复使用集合的contains（）方法
        //boolean[] row = new boolean[m];
        //boolean[] col = new boolean[n];
        TreeSet<Integer> colums = new TreeSet<>();
        TreeSet<Integer> rows = new TreeSet<>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 0) {
                    colums.add(j);
                    rows.add(i);
                }

            }
        }
        for (int i = 0; i < matrix.length; i++) {
            if (rows.contains(i)) {
                matrix[i] = new int[matrix[i].length];
            }
            for (int j = 0; j < matrix[i].length; j++) {
                if (colums.contains(j)) {
                    matrix[i][j] = 0;
                }
            }
        }
    }
    /*public void setZeroes(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        boolean[] row = new boolean[m];
        boolean[] col = new boolean[n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 0) {
                    row[i] = col[j] = true;
                }
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (row[i] || col[j]) {
                    matrix[i][j] = 0;
                }
            }
        }
    }
*/


}
//leetcode submit region end(Prohibit modification and deletion)
