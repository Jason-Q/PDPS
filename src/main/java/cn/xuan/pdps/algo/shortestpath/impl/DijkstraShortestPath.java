package cn.xuan.pdps.algo.shortestpath.impl;

import cn.xuan.pdps.algo.shortestpath.AbstractShortestPath;
import org.junit.Test;

import java.util.*;

public class DijkstraShortestPath extends AbstractShortestPath {
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
    public Map<String, Integer> shortestPathGivenVerticle(final Map<String, List<String>> network, String from) {
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
                Set<String> keySet = network.keySet();
                Set<String> restV = new HashSet<>();
                restV.addAll(keySet);
                restV.removeAll(result.keySet());
                for (String vert : restV) {
                    System.out.println(from + "," + vert);
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

    @Test
    public void dijkstraTest() {
        Map<String, List<String>> network = new HashMap<>();
        String one = "1";
        String two = "2";
        String three = "3";
        String four = "4";
        String five = "5";

        List<String> ones = new ArrayList<>();
        ones.add(two);
        ones.add(three);
        network.put(one, ones);

        List<String> twos = new ArrayList<>();
        twos.add(four);
        twos.add(three);
        network.put(two, twos);

        List<String> threes = new ArrayList<>();
        threes.add(two);
        network.put(three, threes);

        List<String> fours = new ArrayList<>();
        fours.add(five);
        network.put(four, fours);

        List<String> fives = new ArrayList<>();
        network.put(five, fives);

        Map<String, Map<String, Integer>> result = new DijkstraShortestPath().shortestPathAnyVerticles(network);

        System.out.println();

    }
}
