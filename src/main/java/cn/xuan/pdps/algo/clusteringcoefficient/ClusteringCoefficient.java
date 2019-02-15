package cn.xuan.pdps.algo.clusteringcoefficient;

import java.util.List;
import java.util.Map;

/**
 * 计算网络中的平均聚集系数
 */
public interface ClusteringCoefficient {

    /**
     * 计算网络的平均聚集系数
     * @param network
     * @return
     */
    double averageClusteringCoefficient(Map<String, List<String>> network);
}
