package cn.xuan.pdps.algo.shortestpath;

import java.util.List;
import java.util.Map;

public abstract class AbstractShortestPath implements ShortestPath{
    @Override
    public double averageShortestPath(Map<String, List<String>> network) {
        double result = 0;
        Map<String, Map<String, Integer>> shortestPath = shortestPathAnyVerticles(network);
        long count = 0;
        for (Map.Entry<String, Map<String, Integer>> paths : shortestPath.entrySet()) {
            Map<String, Integer> length = paths.getValue();
            for (Integer value : length.values()) {
                if (value != Integer.MAX_VALUE) {
                    result += value;
                    count++;
                }
            }
        }
        result /= count;
        return result;
    }
}
