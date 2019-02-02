package cn.xuan.pdps.algo.skyline.impl;

import cn.xuan.pdps.algo.skyline.AbstractSkylineQuery;
import cn.xuan.pdps.model.DominatedResult; import jdk.nashorn.internal.runtime.linker.LinkerCallSite; import java.util.*;
public class BNL extends AbstractSkylineQuery {
    @Override
    public List<Map<String, List<Double>>> skylineQuery(Map<String, List<Double>> points) {
        List<Map<String, List<Double>>> result = new ArrayList<>();

        while (points.size() > 0) {
            Map<String, List<Double>> skylineCache = new HashMap<>();
            Map<String, List<Double>> restPoints = new HashMap<>();

            Iterator<Map.Entry<String, List<Double>>> pointIterator = points.entrySet().iterator();
            while (pointIterator.hasNext()) {
                Map.Entry<String, List<Double>> point = pointIterator.next();

                DominatedResult dominatedResult = dominated(point.getValue(), skylineCache);
                if (dominatedResult.isDominated()) {
                    restPoints.put(point.getKey(), point.getValue());
                } else {
                    skylineCache.put(point.getKey(), point.getValue());
                    if (null != dominatedResult.getDominateList() &&
                            dominatedResult.getDominateList().size() > 0) {
                        restPoints.putAll(dominatedResult.getDominateList());
                        for (Map.Entry<String, List<Double>> dominatedPoint :
                                dominatedResult.getDominateList().entrySet()) {
                            skylineCache.remove(dominatedPoint.getKey());
                        }
                    }
                }
            }
        }
        return result;
    }
}
