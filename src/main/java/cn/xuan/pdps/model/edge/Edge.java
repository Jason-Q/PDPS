package cn.xuan.pdps.model.edge;

import cn.xuan.pdps.model.Separator;

public class Edge {
    private String _1;
    private String _2;
    private boolean directed = false;


    public Edge() {
    }

    public Edge(String _1, String _2) {
        this._1 = _1;
        this._2 = _2;
    }

    public Edge(String _1, String _2, boolean directed) {
        this._1 = _1;
        this._2 = _2;
        this.directed = directed;
    }

    public String get_1() {
        return _1;
    }

    public void set_1(String _1) {
        this._1 = _1;
    }

    public String get_2() {
        return _2;
    }

    public void set_2(String _2) {
        this._2 = _2;
    }

    public boolean isDirected() {
        return directed;
    }

    public void setDirected(boolean directed) {
        this.directed = directed;
    }

    @Override
    public String toString() {
        return this._1 + Separator.fieSeparator + this._2;
    }
}
