package algorithm.leetcode.editor.cn;//给你一个整数数组 nums ，请你找出一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
//
// 子数组 是数组中的一个连续部分。 
//
// 
//
// 示例 1： 
//
// 
//输入：nums = [-2,1,-3,4,-1,2,1,-5,4]
//输出：6
//解释：连续子数组 [4,-1,2,1] 的和最大，为 6 。
// 示例 2： 
//
// 
//输入：nums = [1]
//输出：1
// 
//
// 示例 3： 
//
// 
//输入：nums = [5,4,-1,7,8]
//输出：23//
// 
//
// 提示： 
//
// 
// 1 <= nums.length <= 105 
// -104 <= nums[i] <= 104
// 进阶：如果你已经实现复杂度为 O(n) 的解法，尝试使用更为精妙的 分治法 求解。 
// Related Topics 数组 分治 动态规划 
// 👍 4675 👎 0


//leetcode submit region begin(Prohibit modification and deletion)
class Solution53 {
    public int maxSubArray(int[] nums) {
        int[] dp = new int[nums.length];
        dp[0] = nums[0];
        int max = dp[0];
        for (int i = 1; i < nums.length; i++) {
            // dp[i-1]小于0，则舍弃之前累计值，它不利于取得最大连续数组
            dp[i] = dp[i - 1] < 0 ? nums[i] : dp[i - 1] + nums[i];
            //dp不是直接存的是累计值，是起伏的，故每次要记录最大值
            max = Math.max(max, dp[i]);

        }
        return max;

    }


}
//leetcode submit region end(Prohibit modification and deletion)
