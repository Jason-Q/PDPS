package cn.xuan.pdps.algo.clusteringcoefficient.impl;

import cn.xuan.pdps.algo.clusteringcoefficient.ClusteringCoefficient;
import cn.xuan.pdps.utils.Network2GraphUtils;
import org.graphstream.algorithm.Toolkit;

import java.util.List;
import java.util.Map;

public class ClusteringCoefficientWithGraphstream implements ClusteringCoefficient {
    @Override
    public double averageClusteringCoefficient(Map<String, List<String>> network) {
        return Toolkit.averageClusteringCoefficient(Network2GraphUtils.castNetwork2Graph(network));
    }
}
