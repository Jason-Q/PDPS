package cn.xuan.pdps.algo.clusteringcoefficient;

import java.util.List;
import java.util.Map;

public interface ClusteringCoefficient {
    double averageClusteringCoefficient(Map<String, List<String>> network);
}
