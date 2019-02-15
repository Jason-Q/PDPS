package cn.xuan.pdps;

import cn.xuan.pdps.algo.clusteringcoefficient.ClusteringCoefficient;
import cn.xuan.pdps.algo.clusteringcoefficient.impl.ClusteringCoefficientWithGraphstream;
import cn.xuan.pdps.algo.pdps.PDPS;
import cn.xuan.pdps.model.SocialNetwork;
import cn.xuan.pdps.utils.SocialNetworkReaderUtils;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        // 构建社交网络
        File file = new File("data/");
//        File pvalueFile = new File("data/pvalue/pvalue.txt");
        SocialNetwork network = SocialNetworkReaderUtils.constructSocialNetworkFromFile(file);
//        SocialNetwork network = SocialNetworkReaderUtils.constructSocialNetworkFromFile(file, pvalueFile.getAbsolutePath());

        // 构建按PDPS并运行
        PDPS pdps = new PDPS(network);
        pdps.run(7, 0.8);

        // 获取原始图
        pdps.getOriginalNetwork();

        // 获取分割图
        pdps.getSplitedNetwork();

        // 获取采样图
        pdps.getDeletedEdges();
        pdps.getRestEdges();

        // 获取加噪图
        pdps.getGeneratedEdges();
        pdps.getRestEdges();

        // 计算原始图与加噪图的平均最短路径

        // 计算原始图与加噪图的聚集系数
        ClusteringCoefficient clusteringCoefficient = new ClusteringCoefficientWithGraphstream();
        clusteringCoefficient.averageClusteringCoefficient(network.getEdges());

        // 计算数据缺失率
        pdps.calMissRate();

        System.out.println();
    }
}
