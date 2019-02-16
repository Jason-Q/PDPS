package cn.xuan.pdps.utils;

import cn.xuan.pdps.model.edge.Edge;

import java.util.*;

public class Edge2NetworkUtils {

    /**
     * 将边的集合转换为邻接表
     * @param edges
     * @return
     */
    public static Map<String, List<String>> convertEdges2Network(Set<Edge>... edges) {
        Map<String, List<String>> result = new HashMap<>();
        for (int i = 0; i < edges.length; i++) {
            Set<Edge> edgeSet = edges[i];
            for (Edge edge : edgeSet) {
                addEdge(result, edge);
            }
        }
        return result;
    }

    private static void addEdge(Map<String, List<String>> network, Edge edge) {
        addEdge(network, edge.get_1(), edge.get_2());
        if (!edge.isDirected()) {
            addEdge(network, edge.get_2(), edge.get_1());
        }
    }

    private static void addEdge(Map<String, List<String>> network, String from, String to) {
        if (network.containsKey(from) && null != network.get(from)) {
            network.get(from).add(to);
        } else {
            List<String> toNodes = new ArrayList<>();
            toNodes.add(to);
            network.put(from, toNodes);
        }
    }
}
