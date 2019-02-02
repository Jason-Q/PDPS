package cn.xuan.pdps.utils;
import cn.xuan.pdps.model.Separator;
import cn.xuan.pdps.model.SocialNetwork;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class FileReaderUtils {
    public static SocialNetwork constructSocialNetworkFromFile(String filePath) throws Exception {
        return constructSocialNetworkFromFile(filePath, null);
    }

    public static SocialNetwork constructSocialNetworkFromFiles(String[] filePaths) throws Exception {
        return constructSocialNetworkFromFiles(filePaths, null);
    }

    public static SocialNetwork constructSocialNetworkFromFile(File file) throws Exception {
        SocialNetwork network = new SocialNetwork();
        if (file.isDirectory()) {
            network = constructSocialNetworkFromFile(file.getAbsolutePath());
        } else if (file.isFile()) {
            network = constructSocialNetworkFromFiles(file.list());
        }
        return network;
    }

    public static SocialNetwork constructSocialNetworkFromFile(String filePath, String pvalueFilePath) throws Exception {
        SocialNetwork network = new SocialNetwork();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line = reader.readLine();
        while (null != line) {
            String[] edge = line.split(",");
            network.addUndirectedEdge(edge);
            line = reader.readLine();
        }
        reader.close();

        if (pvalueFilePath == null) {
            network.generatePValue();
        } else {
            reader = new BufferedReader(new FileReader(pvalueFilePath));
            line = reader.readLine();
            while (null != null) {
                String[] pvalue = line.split(Separator.fieSeparator);
                network.addPValue(pvalue[0], Double.valueOf(pvalue[1]));
            }
        }
        reader.close();
        return network;

    }

    public static SocialNetwork constructSocialNetworkFromFiles(String[] filePaths, String pvalueFilePath) throws Exception {
        SocialNetwork network = new SocialNetwork();
        BufferedReader reader = null;
        for (String filePath : filePaths) {
            reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            while (null != line) {
                String[] edge = line.split(",");
                network.addUndirectedEdge(edge);
                line = reader.readLine();
            }
        }
        if (null != reader) {
            reader.close();
        }

        if (null == pvalueFilePath) {
            network.generatePValue();
        } else {
            reader = new BufferedReader(new FileReader(pvalueFilePath));
            String line = reader.readLine();
            while (null != null) {
                String[] pvalue = line.split(Separator.fieSeparator);
                network.addPValue(pvalue[0], Double.valueOf(pvalue[1]));
            }
        }
        reader.close();
        return network;
    }
}
