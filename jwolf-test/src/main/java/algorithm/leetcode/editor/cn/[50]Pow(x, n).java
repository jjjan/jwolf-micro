package algorithm.leetcode.editor.cn;//实现 pow(x, n) ，即计算 x 的 n 次幂函数（即，xn ）。
//
// 
//
// 示例 1： 
//
// 
//输入：x = 2.00000, n = 10
//输出：1024.00000
// 
//
// 示例 2： 
//
// 
//输入：x = 2.10000, n = 3
//输出：9.26100
// 
//
// 示例 3： 
//
// 
//输入：x = 2.00000, n = -2
//输出：0.25000
//解释：2-2 = 1/22 = 1/4 = 0.25
// 
//
// 
//
// 提示： 
//
// 
// -100.0 < x < 100.0 
// -231 <= n <= 231-1 
// -104 <= xn <= 104 
// 
// Related Topics 递归 数学 
// 👍 922 👎 0


//leetcode submit region begin(Prohibit modification and deletion)

/**
 * 1.直接递归会存在栈溢出问，需要降低递归次数，这里考虑快速幂：pow(x,n) ->pow(x*x,n/2)
 * 2.n=-2147483648转为相反数会溢出，需要赋值给long N
 *
 */
class Solution50 {
     public double myPow(double x, int n) {
         long N=n;
         return N >= 0 ? positivePow(x, N) : 1.0 / positivePow(x, -N);


    }

    public double positivePow(double x, long n) {
        if (n == 0) return 1.0;
        if (n % 2 == 0) {
            return positivePow(x * x, n / 2);
        }
        return x * myPow(x, (int)(n) - 1);

    }

    public static void main(String[] args) {
        double v = new Solution50().myPow(2.0, -2147483648);
        System.out.println(v);
    }
}
/*    public double myPow(double x, int n) {
        if (n == 0) {
            return 1;
        } else if (n < 0) {
            double v = positivePow(x, -n);
            return 1 / v;
        } else {
            return positivePow(x, n);
        }

    }

    public double positivePow(double x, int n) {
        while (n > 1) {
            return x * myPow(x, n - 1);
        }
        return x;
    }
*/
//leetcode submit region end(Prohibit modification and deletion)
