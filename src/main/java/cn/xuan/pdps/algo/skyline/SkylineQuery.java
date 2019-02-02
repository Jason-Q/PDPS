package cn.xuan.pdps.algo.skyline;

import cn.xuan.pdps.model.DominatedResult;

import java.util.List;
import java.util.Map;

public interface SkylineQuery {

    List<Map<String, List<Double>>> skylineQuery(Map<String, List<Double>> points);

    DominatedResult dominated(List<Double> point, Map<String, List<Double>> skylineCache);
}
