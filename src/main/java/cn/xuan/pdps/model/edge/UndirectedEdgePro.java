package cn.xuan.pdps.model.edge;

public class UndirectedEdgePro extends UndirectedEdge{
    private double pro;

    public UndirectedEdgePro() {
    }

    public UndirectedEdgePro(Edge edge, double pro) {
        super(edge.get_1(), edge.get_2());
        this.pro = pro;
    }

    public double getPro() {
        return pro;
    }

    public void setPro(double pro) {
        this.pro = pro;
    }
}
