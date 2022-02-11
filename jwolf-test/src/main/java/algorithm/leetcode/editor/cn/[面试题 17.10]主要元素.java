package algorithm.leetcode.editor.cn;//数组中占比超过一半的元素称之为主要元素。给你一个 整数 数组，找出其中的主要元素。若没有，返回 -1 。请设计时间复杂度为 O(N) 、空间复杂度为 O(1
//) 的解决方案。 
//
// 
//
// 示例 1： 
//
// 
//输入：[1,2,5,9,5,9,5,5,5]
//输出：5 
//
// 示例 2： 
//
// 
//输入：[3,2]
//输出：-1 
//
// 示例 3： 
//
// 
//输入：[2,2,1,1,1,2,2]
//输出：2 
// Related Topics 数组 计数 
// 👍 195 👎 0


import java.util.HashMap;

//leetcode submit region begin(Prohibit modification and deletion)
class Solution1710 {
    public static void main(String[] args) {
        int[] ints = {2, 2, 1, 1, 1, 2};
        int i = new Solution1710().majorityElement(ints);
    }

    public int majorityElement(int[] nums) {

        int halfLen = nums.length / 2;
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            Integer integer = map.getOrDefault(nums[i], 0) + 1;
            map.put(nums[i], integer);
            if (integer > halfLen) {
                return nums[i];
            }
        }

        return -1;
    }
}
//leetcode submit region end(Prohibit modification and deletion)
