package cn.xuan.pdps.algo.skyline;

import cn.xuan.pdps.model.DominatedResult;

import java.util.List;
import java.util.Map;

/**
 * skyline query算法
 */
public interface SkylineQuery {

    /**
     * 计算节点的skyline层级
     * @param points
     * @return
     */
    List<Map<String, List<Double>>> skylineQuery(Map<String, List<Double>> points);

    /**
     * 判断point是否被skylineCache中的节点支配
     * @param point
     * @param skylineCache
     * @return
     */
    DominatedResult dominated(List<Double> point, Map<String, List<Double>> skylineCache);
}
