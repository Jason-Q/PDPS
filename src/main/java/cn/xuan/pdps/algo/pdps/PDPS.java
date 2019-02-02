package cn.xuan.pdps.algo.pdps;

import cn.xuan.pdps.algo.skyline.impl.BNL;
import cn.xuan.pdps.model.SocialNetwork;
import cn.xuan.pdps.model.edge.Edge;
import cn.xuan.pdps.model.edge.UndirectedEdgePro;

import java.util.*;

public class PDPS {
    public static void pdps(SocialNetwork network, int k) throws Exception {
        Map<String, List<Double>> featureVectors = calFeatureVector(network);

        List<Map<String, List<Double>>> skylineLevel = skyline(featureVectors);

        List<Map<String, List<Double>>> privateLevel = cast2KDiem(k, skylineLevel);
    }

    private static Map<String, List<Double>> calFeatureVector(SocialNetwork network) throws Exception {
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

    private static void addFeature(Map<String, List<Double>> featureVector, String verticle, Double featureValue) {
        if (featureVector.containsKey(verticle) && null != featureVector.get(verticle)) {
            featureVector.get(verticle).add(featureValue);
        } else {
            List<Double> features = new ArrayList<>();
            features.add(featureValue);
            featureVector.put(verticle, features);
        }
    }

    private static List<Map<String, List<Double>>> skyline(Map<String, List<Double>> points) {
        BNL bnl = new BNL();
        return bnl.skylineQuery(points);
    }

    private static List<Map<String, List<Double>>> cast2KDiem(int k, List<Map<String, List<Double>>> privateLevel) {
        List<Map<String, List<Double>>> result = new HashMap<>();

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
        for (int i = 0; i < originalDime; i++) {
            newLevel.putAll(privateLevel.get(win * (k - 1) + i));
        }
        result.add(newLevel);
        return result;
    }

    private List<List<UndirectedEdgePro>> calEdgePro(SocialNetwork network, double threshold) {
        Set<Edge> edges = network.getAllEdges();
    }
}
