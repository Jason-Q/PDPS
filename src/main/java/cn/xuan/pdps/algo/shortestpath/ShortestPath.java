package cn.xuan.pdps.algo.shortestpath;

import java.util.List;
import java.util.Map;

/**
 * 计算网络中的最短路径，不支持边的权重
 */
public interface ShortestPath {

    /**
     * 计算网络中任意两点间的最短路径
     * @param network
     * @return
     */
    Map<String, Map<String, Integer>> shortestPathAnyVerticles(Map<String, List<String>> network);

    /**
     * 计算网络中节点fro到其余所哟节点的最短路径
     * @param network
     * @param from
     * @return
     */
    Map<String, Integer> shortestPathGivenVerticle(Map<String, List<String>> network, String from);

    /**
     * 计算网络的平均最短路径
     * @param network
     * @return
     */
    double averageShortestPath(Map<String, List<String>> network);
}