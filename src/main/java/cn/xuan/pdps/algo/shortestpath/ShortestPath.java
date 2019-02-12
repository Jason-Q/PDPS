package cn.xuan.pdps.algo.shortestpath;

import java.util.List;
import java.util.Map;

public interface ShortestPath {
    Map<String, Map<String, Integer>> shortestPathAnyVerticles(Map<String, List<String>> network);

    Map<String, Integer> shortestPathGivenVerticle(Map<String, List<String>> network, String from);
}