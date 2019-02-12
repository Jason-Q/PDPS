package cn.xuan.pdps.utils;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.List;
import java.util.Map;

public class Network2GraphUtils {
    public static Graph castNetwork2Graph(Map<String, List<String>> network) {
        Graph result = new SingleGraph("social network");
        result.setStrict(false);
        result.setAutoCreate(true);

        for (Map.Entry<String, List<String>> edge : network.entrySet()) {
            String fromNode = edge.getKey();
            List<String> toNodes = edge.getValue();
            for (int i = 0; i < toNodes.size(); i++) {
                String toNode = toNodes.get(i);
                result.addEdge(fromNode + toNode, fromNode, toNode);
            }
        }

        return result;
    }
}
