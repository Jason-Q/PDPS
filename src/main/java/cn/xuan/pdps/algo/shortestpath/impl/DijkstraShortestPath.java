package cn.xuan.pdps.algo.shortestpath.impl;

import cn.xuan.pdps.algo.shortestpath.ShortestPath;
import org.junit.internal.runners.statements.FailOnTimeout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DijkstraShortestPath implements ShortestPath {
    @Override
    public Map<String, Map<String, Integer>> shortestPathAnyVerticles(Map<String, List<String>> network) {
        Map<String, Map<String, Integer>> result = new HashMap<>();

        for (String fromNode : network.keySet()) {
            Map<String, Integer> path = shortestPathGivenVerticle(network, fromNode);
            result.put(fromNode, path);
        }

        return result;
    }

    @Override
    public Map<String, Integer> shortestPathGivenVerticle(Map<String, List<String>> network, String from) {
        Map<String, Integer> result = new HashMap<>();

        Map<String, Integer> path = new HashMap<>();
        path.put(from, 0);

        for (int i = 0; i < network.size(); i++) {
            int min = Integer.MAX_VALUE;
            String minV = "";
            for (String verticle : path.keySet()) {
                if (path.get(verticle) < min) {
                    min = path.get(verticle);
                    minV = verticle;
                }
            }

            if (path.size() == 0) {
                Set<String> restV = network.keySet();
                restV.removeAll(result.keySet());
                for (String vert : restV) {
                    result.put(vert, Integer.MAX_VALUE);
                }
                break;
            }

            result.put(minV, min);
            path.remove(minV);

            for (int j = 0; j < network.get(minV).size(); j++) {
                String verticle = network.get(minV).get(j);
                if (!result.containsKey(verticle) &&
                        (!path.containsKey(verticle) || (min + 1) < path.get(verticle))) {
                    path.put(verticle, min + 1);
                }
            }
        }
        return result;
    }
}
