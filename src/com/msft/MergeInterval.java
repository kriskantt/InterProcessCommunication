package com.msft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

class CustomComparator implements Comparator<int[]> {
    public int compare(int[] a, int[] b) {
        return a[0]-b[0];
    }
}
public class MergeInterval {
    public int[][] merge(int[][] intervals) {
        ArrayList<ArrayList<Integer>> arr = new ArrayList<>();
        int len = intervals.length;

        Arrays.sort(intervals, (a,b)->(a[0]-b[0]));

        int start = intervals[0][0];
        int end = intervals[0][1];
        for(int i=1;i<len;i++) {
            if(end<intervals[i][0]) {
                ArrayList<Integer> tmp = new ArrayList<>();
                tmp.add(start);
                tmp.add(end);
                arr.add(tmp);
                start = intervals[i][0];
                end = intervals[i][1];
            } else {
                end = Math.max(end, intervals[i][1]);
            }
        }
        ArrayList<Integer> tmp = new ArrayList<>();
        tmp.add(start);
        tmp.add(end);
        arr.add(tmp);

        int[][] ans = new int[arr.size()][2];
        for(int i=0;i<arr.size();i++) {
            ans[i][0] = arr.get(i).get(0);
            ans[i][1] = arr.get(i).get(1);
        }
        return ans;
    }
    public static void main(String[] args) {

    }
}
