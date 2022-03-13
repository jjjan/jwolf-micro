package algorithm.leetcode.editor.cn;//给定一个字符串，验证它是否是回文串，只考虑字母和数字字符，可以忽略字母的大小写。
//
// 说明：本题中，我们将空字符串定义为有效的回文串。 
//
// 
//
// 示例 1: 
//
// 
//输入: "A man, a plan, a canal: Panama"
//输出: true
//解释："amanaplanacanalpanama" 是回文串
// 
//
// 示例 2: 
//
// 
//输入: "race a car"
//输出: false
//解释："raceacar" 不是回文串
// 
//
// 
//
// 提示： 
//
// 
// 1 <= s.length <= 2 * 105 
// 字符串 s 由 ASCII 字符组成 
// 
// Related Topics 双指针 字符串 
// 👍 490 👎 0


//leetcode submit region begin(Prohibit modification and deletion)
class Solution {
    public boolean isPalindrome(String s) {

        int right = s.length() - 1;
        int left = 0;
        while (left < right) {
            char r = s.charAt(right);
            char l = s.charAt(left);
            if (!isZimu(l) && !isNum(l)) {
                left++;
                continue;
            }
            if (!isZimu(r) && !isNum(r)) {
                right--;
                continue;
            }
            if (isZimu(r) && isZimu(l) && (r - 32 == l || r == l - 32) || l == r) {
                right--;
                left++;
            } else {
                return false;
            }
        }

        return true;
    }

    public static boolean isNum(char c) {
        return c >= '0' && c <= '9';
    }

    public static boolean isZimu(char l) {
        return (l >= 'a' && l <= 'z') || (l >= 'A' && l <= 'Z');
    }


}
//leetcode submit region end(Prohibit modification and deletion)
