package cn.xuan.pdps.utils;

import cn.xuan.pdps.model.edge.Edge;
import cn.xuan.pdps.model.SocialNetwork;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

public class FileWriterUtils {
    public static void write(SocialNetwork network, String path) throws IOException {
        Set<Edge> edges = network.getAllEdges();
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        for (Edge edge : edges) {
            writer.write(edge.toString());
            writer.newLine();
        }
        writer.flush();
        writer.close();
    }
}
