package cn.xuan.pdps.algo.pdps;

import cn.xuan.pdps.algo.skyline.impl.BNL;
import cn.xuan.pdps.model.SocialNetwork;
import cn.xuan.pdps.model.edge.Edge;
import cn.xuan.pdps.model.edge.UndirectedEdge;
import cn.xuan.pdps.model.edge.UndirectedEdgePro;

import java.io.File;
import java.util.*;

/**
 * PDPS 算法
 */
public class PDPS {
    /**
     * 社交网络
     */
    private SocialNetwork network;

    /**
     * 网络节点的特征向量，String表示节点，List<Double>标识特征向量
     */
    private Map<String, List<Double>> featureVector;

    /**
     * 节点隐私泄露等级，splitedNetwork.ger(0)表示隐私泄露等级为1的节点集合
     */
    private List<Set<Edge>> splitedNetwork;

    /**
     * 采样边的集合，即删除的边的集合
     */
    private Set<Edge> deletedEdges;

    /**
     * 剩余边的集合，即原网络边集合 - 采样边集合
     */
    private Set<Edge> restEdges;

    /**
     * 生成边的集合，噪音边
     */
    private Set<Edge> generatedEdges;

    /**
     * 加噪图文件
     */
    private File dstFile;

    public PDPS(SocialNetwork network) {
        this.network = network;
    }

    /**
     * 获取原始网络的所有边,
     * @see SocialNetwork#getEdges()
     * @return
     */
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

    /**
     * 运行pdps算法，并将加噪图保存在文件中
     * @param k 隐私泄露等级划分参数
     * @param threshold 边采样参数
     * @throws Exception
     */
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

        //todo: 加噪图保存在文件中
    }

    /**
     * 计算PDPS算法的缺失率
     * @return 数据缺失率
     * @throws Exception
     */
    public double calMissRate() throws Exception {
        if (deletedEdges == null) {
            throw new Exception("Not run algotithm PDPS");
        }

        return restEdges.size() / (restEdges.size() + deletedEdges.size());
    }

    /**
     * 计算网络中所有节点的特征向量(CF1, CD2, P, T)
     * @return
     * @throws Exception
     */
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

    /**
     * 依据所有节点的特征向量，计算skyline(即隐私泄露等级)
     * @param points skyline层级
     * @return
     */
    private List<Map<String, List<Double>>> skyline(Map<String, List<Double>> points) {
        BNL bnl = new BNL();
        return bnl.skylineQuery(points);
    }

    /**
     * 将隐私泄露等级划分为k个等级，每个等级的个数为
     * (privateLevel.size/k, privateLevel.size/k,...,privateLevel.size/k+privateLevel.size%k)
     * @param k 隐私泄露等级个数
     * @param privateLevel 原有的隐私泄露等级
     * @return 新的隐私泄露等级
     */
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

    /**
     * 计算每条边被采样的概率，其中一条边有可能出现在多个隐私泄露等级中。
     * 例如，边(e1, e2)，其中e1隐私泄露等级为1，e2的隐私泄露等级为2，则边(e1, e2)
     * 会出现在隐私等级为1与2中。其中隐私泄露等级对应List的index，边的采
     * 样概率存储在UndirectedEdgePro
     * @param newLevel 隐私泄露等级
     * @param threshold 采样概率阈值
     * @return 每条边对应的隐私泄露等级及被采样的概率
     */
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

    /**
     * 按照边的采样概率每个隐私泄露等级中的边升序排序
     * @param edgesPro 每条边的额采样概率
     * @return 排序后的边的采样概率
     */
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

    /**
     * 边采样，即删除容易泄露隐私的边
     * @param sortedEdgesPro
     * @param k
     * @return 被采样(删除)的边
     */
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

    /**
     * 生成随机边
     * @param generatedSize 要生成的边的数量
     * @param deletedEdges
     * @return 生成的随即边
     */
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
