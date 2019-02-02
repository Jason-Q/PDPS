package cn.xuan.pdps.model.edge;

import java.util.Objects;

public class UndirectedEdge extends Edge {
    public UndirectedEdge() {
        this.setDirected(false);
    }

    public UndirectedEdge(String _1, String _2) {
        super(_1, _2, false);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return isDirected() == edge.isDirected() &&
                ((this.get_1().equals(edge.get_1()) && this.get_2().equals(edge.get_2())) ||
                        this.get_1().equals(edge.get_2()) && this.get_2().equals(edge.get_1()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.get_1(), this.get_2(), this.isDirected());
    }
}
