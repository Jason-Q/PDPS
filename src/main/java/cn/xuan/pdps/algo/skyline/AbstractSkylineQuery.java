package cn.xuan.pdps.algo.skyline;

import cn.xuan.pdps.model.DominatedResult;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class AbstractSkylineQuery implements SkylineQuery{

    @Override
    public DominatedResult dominated(List<Double> point, Map<String, List<Double>> skylineCache) {

        DominatedResult result = new DominatedResult();

        if (null == skylineCache || skylineCache.size() < 1) {
            result.setDominated(false);
            return result;
        }

        Iterator<Map.Entry<String, List<Double>>> iterator = skylineCache.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<Double>> skylinePoint = iterator.next();
            String key = skylinePoint.getKey();
            List<Double> vector = skylinePoint.getValue();

            boolean dominated = true;
            for (int i = 0; i < vector.size(); i++) {
                if (vector.get(i) > point.get(i)) {
                    dominated = false;
                    break;
                }
            }

            boolean dominate = true;
            for (int i = 0; i < point.size(); i++) {
                if (point.get(i) > vector.get(i)) {
                    dominate = false;
                    break;
                }
            }

            result.setDominated(dominated);
            if (dominate) {
                if (null != result.getDominateList()) {
                    result.getDominateList().put(key, vector);
                } else {
                    Map<String, List<Double>> map = new HashMap<>();
                    map.put(key, vector);
                    result.setDominateList(map);
                }
            }
        }
        return result;
    }
}
