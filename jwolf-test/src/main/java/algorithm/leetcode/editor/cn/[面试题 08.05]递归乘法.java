package algorithm.leetcode.editor.cn;//递归乘法。 写一个递归函数，不使用 * 运算符， 实现两个正整数的相乘。可以使用加号、减号、位移，但要吝啬一些。
//
// 示例1: 
//
// 
// 输入：A = 1, B = 10
// 输出：10
// 
//
// 示例2: 
//
// 
// 输入：A = 3, B = 4
// 输出：12
// 
//
// 提示: 
//
// 
// 保证乘法范围不会溢出 
// 
// Related Topics 位运算 递归 数学 
// 👍 62 👎 0


//leetcode submit region begin(Prohibit modification and deletion)
class Solution递归乘法 {
    public int multiply(int A, int B) {
        int b = B < 0 ? -B : B;
        int res = 0;
        while (b > 0) {
            res += A;
            b--;
        }
        return B < 0 ? -res : res;
    }

    public static void main(String[] args) {
        System.out.println(new Solution递归乘法().multiply(-2, 0));
    }
}
//leetcode submit region end(Prohibit modification and deletion)
