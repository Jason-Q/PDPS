package cn.xuan.pdps.algo.pdps;

import cn.xuan.pdps.algo.skyline.impl.BNL;
import cn.xuan.pdps.model.SocialNetwork;
import cn.xuan.pdps.model.edge.Edge;
import cn.xuan.pdps.model.edge.UndirectedEdge;
import cn.xuan.pdps.model.edge.UndirectedEdgePro;
import cn.xuan.pdps.utils.Network2GraphUtils;
import com.sun.xml.internal.bind.v2.model.core.ID;
import org.apache.commons.math.ode.sampling.StepInterpolator;

import java.util.*;

public class PDPS {
    private SocialNetwork network;
    
    private Map<String, List<Double>> featureVector;
    
    private List<Set<Edge>> splitedNetwork;
    
    private Set<Edge> deletedEdges;
    
    private Set<Edge> restEdges;
    
    private Set<Edge> generatedEdges;

    public PDPS(SocialNetwork network) {
        this.network = network;
    }

    public Set<Edge> getOriginalNetwork() {
        if (null == network) {
            return null;
        }
        return network.getAllEdges();
    }

    public Map<String, List<Double>> getFeatureVector() {
        return featureVector;
    }

    public List<Set<Edge>> getSplitedNetwork() {
        return splitedNetwork;
    }

    public Set<Edge> getDeletedEdges() {
        return deletedEdges;
    }

    public Set<Edge> getRestEdges() {
        return restEdges;
    }

    public Set<Edge> getGeneratedEdges() {
        return generatedEdges;
    }

    public void run(int k, double threshold) throws Exception {
        featureVector = calFeatureVector();

        List<Map<String, List<Double>>> skylineLevel = skyline(featureVector);

        List<Map<String, List<Double>>> privateLevel = cast2KDiem(k, skylineLevel);

        splitedNetwork = new ArrayList<>();
        for (int i = 0; i < privateLevel.size(); i++) {
            Set<String> nodes = (Set<String>) privateLevel.get(i).keySet();
            Set<Edge> splitedEdge = network.getEdges(nodes);
            splitedNetwork.add(splitedEdge);
        }

        List<Set<UndirectedEdgePro>> edgesPro = calEdgePro(privateLevel, threshold);

        List<List<UndirectedEdgePro>> sortedEdgesPro = sortEdgesProWithPro(edgesPro);

        deletedEdges = sampleEdges(sortedEdgesPro, k);

        restEdges = network.getAllEdges();
        restEdges.removeAll(deletedEdges);

        int deletedEdgesSize = deletedEdges.size();

        generatedEdges = generateEdges(deletedEdgesSize, deletedEdges);
    }

    public double calMissRate() throws Exception {
        if (deletedEdges == null) {
            throw new Exception("MEIYOU YUNXING");
        }

        return restEdges.size() / (restEdges.size() + deletedEdges.size());
    }

    private Map<String, List<Double>> calFeatureVector() throws Exception {
        Map<String, List<Double>> featureVector = new HashMap<>();
        int vnum = network.getEdges().size();
        for (Map.Entry<String, List<String>> edges : network.getEdges().entrySet()) {
            String verticle = edges.getKey();
            // CFP1
            addFeature(featureVector, verticle, (double) edges.getValue().size());

            // CFP2
            Set<String> verticles = new HashSet<>();
            verticles.addAll(edges.getValue());
            for (int i = 0; i < edges.getValue().size(); i++) {
                String node = edges.getValue().get(i);
                verticles.addAll(network.getEdges().get(node));
            }
            addFeature(featureVector, verticle, (double) verticles.size());

            // p值
            Double pvalue = network.getPValue(verticle);
            if (null == pvalue) {
                throw new Exception("节点 " + verticle + " 缺少p值");
            }
            addFeature(featureVector, verticle, pvalue);

            // T值
            double t = edges.getValue().size() * pvalue / vnum;
            addFeature(featureVector, verticle, t);
        }
        return featureVector;
    }

    private void addFeature(Map<String, List<Double>> featureVector, String verticle, Double featureValue) {
        if (featureVector.containsKey(verticle) && null != featureVector.get(verticle)) {
            featureVector.get(verticle).add(featureValue);
        } else {
            List<Double> features = new ArrayList<>();
            features.add(featureValue);
            featureVector.put(verticle, features);
        }
    }

    private List<Map<String, List<Double>>> skyline(Map<String, List<Double>> points) {
        BNL bnl = new BNL();
        return bnl.skylineQuery(points);
    }

    private List<Map<String, List<Double>>> cast2KDiem(int k, List<Map<String, List<Double>>> privateLevel) {
        List<Map<String, List<Double>>> result = new ArrayList<>();

        int originalDime = privateLevel.size();
        int win = originalDime / k;
        for (int i = 0; i < k - 1; i++) {
            Map<String, List<Double>> newLevel = new HashMap<>();
            for (int j = 0; j < win; j++) {
                newLevel.putAll(privateLevel.get(i * win + j));
            }
            result.add(newLevel);
        }

        Map<String, List<Double>> newLevel = new HashMap<>();
        for (int i = win * (k - 1); i < originalDime; i++) {
            newLevel.putAll(privateLevel.get(i));
        }
        result.add(newLevel);
        return result;
    }

    private List<Set<UndirectedEdgePro>> calEdgePro(List<Map<String, List<Double>>> newLevel, double threshold) {
        List<Set<UndirectedEdgePro>> result = new ArrayList<>();
        for (int i = 0; i < newLevel.size(); i++) {
            Set<String> levelKey = newLevel.get(i).keySet();
            Set<Edge> edges = network.getEdges(levelKey);
            Set<UndirectedEdgePro> set = new HashSet<>();
            for (Edge edge : edges) {
                double min = Math.min(network.getPValue(edge.get_1()), network.getPValue(edge.get_2()));

                double pro = 1;
                if (min < threshold) {
                    pro = (Math.exp(min) - 1) / (Math.exp(threshold) - 1);
                }
                UndirectedEdgePro edgePro = new UndirectedEdgePro(edge, pro);
                set.add(edgePro);
            }
            result.add(set);
        }

        return result;
    }

    private List<List<UndirectedEdgePro>> sortEdgesProWithPro(List<Set<UndirectedEdgePro>> edgesPro) {
        List<List<UndirectedEdgePro>> result = new ArrayList<>();
        for (int i = 0; i < edgesPro.size(); i++) {
            List<UndirectedEdgePro> edgePros = new ArrayList<>();
            edgePros.addAll(edgesPro.get(i));
            Collections.sort(edgePros, new Comparator<UndirectedEdgePro>() {
                @Override
                public int compare(UndirectedEdgePro o1, UndirectedEdgePro o2) {
                    if (o1.getPro() == o2.getPro()) {
                        return 0;
                    } else if (o1.getPro() - o2.getPro() > 0) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });
            result.add(edgePros);
        }
        return result;
    }

    private Set<Edge> sampleEdges(List<List<UndirectedEdgePro>> sortedEdgesPro, int k) {
        Set<Edge> result = new HashSet<>();
        double percentage = 0.9;
        for (int i = 0; percentage > 0 && k > i && i < sortedEdgesPro.size(); i++, percentage -= 0.05 * i) {
            int size = sortedEdgesPro.get(i).size();
            int deletedSize = (int) (size * (1 - percentage));
            for (int j = 0; j < deletedSize; j++) {
                result.add(sortedEdgesPro.get(i).get(j));
            }
        }
        return result;
    }

    private Set<Edge> generateEdges(int generatedSize, Set<Edge> deletedEdges) {
        Set<Edge> result = new HashSet<>();
        Set<String> nodes = network.getEdges().keySet();
        int nodesNum = nodes.size();
        while (result.size() < generatedSize) {
            Edge edge = new UndirectedEdge(String.valueOf((int) (Math.random() * nodesNum)),
                    String.valueOf((int) (Math.random() * nodesNum)));
            if (!deletedEdges.contains(edge)) {
                result.add(edge);
            }
        }

        return result;
    }
}
