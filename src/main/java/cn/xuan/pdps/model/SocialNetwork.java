package cn.xuan.pdps.model;

import cn.xuan.pdps.model.edge.DirectedEdge;
import cn.xuan.pdps.model.edge.Edge;
import cn.xuan.pdps.model.edge.UndirectedEdge;

import java.text.DecimalFormat;
import java.util.*;

public class SocialNetwork {
    private boolean directed = false;
    private Map<String, List<String>> edges = new HashMap<String, List<String>>();
    private Map<String, Double> pvalues = new HashMap<>();

    public SocialNetwork() {
    }

    public SocialNetwork(boolean isDirected) {
        this.directed = isDirected;
    }

    public Map<String, List<String>> getEdges() {
        return edges;
    }

    public boolean isDirected() {
        return directed;
    }

    public void setDirected(boolean directed) {
        this.directed = directed;
    }

    public void setEdges(Map<String, List<String>> edges) {
        this.edges = edges;
    }

    public void addDirectedEdge(String fromNode, String endNode) {
        if (edges == null) {
            edges = new HashMap<String, List<String>>();
        }

        if (edges.containsKey(fromNode) && null != edges.get(fromNode)) {
            edges.get(fromNode).add(endNode);
        } else {
            List<String> endNodes = new ArrayList<String>();
            endNodes.add(endNode);
            edges.put(fromNode, endNodes);
        }
    }

    public void addUndirectedEdge(String[] edge) throws Exception {
        if (edge.length != 2) {
            throw new Exception("社交网络边格式错误");
        }
        addDirectedEdge(edge[0], edge[1]);
        addDirectedEdge(edge[1], edge[0]);
    }

    public Set<Edge> getAllEdges() {
        Set<Edge> edges = new HashSet<>();
        boolean directed = this.isDirected();

        if (isDirected()) {
            for (Map.Entry<String, List<String>> edge : this.edges.entrySet()) {
                String from = edge.getKey();
                if (null != edge.getValue()) {
                    for (int i = 0; i < edge.getValue().size(); i++) {
                        String to = edge.getValue().get(i);
                        edges.add(new DirectedEdge(from, to));
                    }
                }
            }
        } else {
            for (Map.Entry<String, List<String>> edge : this.edges.entrySet()) {
                String from = edge.getKey();
                if (null != edge.getValue())
                    for (int i = 0; i < edge.getValue().size(); i++) {
                        String to = edge.getValue().get(i);
                        edges.add(new UndirectedEdge(from, to));
                    }
            }

        }
        return edges;
    }

    public Double getPValue(String verticle) {
        return pvalues.get(verticle);
    }

    public void addPValue(String verticle, Double pvalue) {
        if (null == this.pvalues) {
            this.pvalues = new HashMap<>();
        }
        pvalues.put(verticle, pvalue);
    }

    public void generatePValue() {
        if (null == edges || edges.size() == 0) {
            return;
        }
        DecimalFormat df = new DecimalFormat("#.0");
        for (String verticle : edges.keySet()) {
            pvalues.put(verticle, Double.valueOf(df.format(Math.random())));
        }
    }
}
