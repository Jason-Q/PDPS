package cn.xuan.pdps.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DominatedResult {
    private boolean dominated;
    private Map<String, List<Double>> dominateList;

    public DominatedResult() {
        this.dominated = true;
        dominateList = new HashMap<>();
    }

    public DominatedResult(boolean dominated, Map<String, List<Double>> dominateList) {
        this.dominated = dominated;
        this.dominateList = dominateList;
    }

    public boolean isDominated() {
        return dominated;
    }

    public void setDominated(boolean dominated) {
        this.dominated = dominated;
    }

    public Map<String, List<Double>> getDominateList() {
        return dominateList;
    }

    public void setDominateList(Map<String, List<Double>> dominateList) {
        this.dominateList = dominateList;
    }
}
