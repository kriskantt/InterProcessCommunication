package com.msft;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class TopKFreq {
    public int[] topKFrequent(int[] nums, int k) {
        HashMap<Integer, Integer> cnt = new HashMap<>();
        for(int num: nums) {
            cnt.put(num, cnt.getOrDefault(num,0)+1);
        }

        PriorityQueue<Integer> pq = new PriorityQueue<>((a,b) -> cnt.get(b)-cnt.get(a));
        for(Map.Entry<Integer, Integer> e: cnt.entrySet()) {
           pq.add(e.getKey());
           if(pq.size()>k) pq.poll();
        }

        int[] freq = new int[k];
        int i=0;
        while(!pq.isEmpty()) {
            freq[i++] = pq.poll();
        }
        return freq;
    }
    public static void main(String[] args) {

    }
}
