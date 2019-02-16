package cn.xuan.pdps.utils;

import cn.xuan.pdps.model.SocialNetwork;
import cn.xuan.pdps.model.edge.Edge;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

public class SocialNetworkWriterUtils {
    public static void write(SocialNetwork network, String path) throws IOException {
        Set<Edge> edges = network.getAllEdges();
        write(new File(path), edges);
    }

    public static void write(File file, Set<Edge>... edges) throws IOException {
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        for (int i = 0; i < edges.length; i++) {
            Set<Edge> edgeSet = edges[i];
            for (Edge edge : edgeSet) {
                writer.write(edge.toString());
                writer.newLine();
            }
        }
        writer.flush();
        writer.close();
    }
}
