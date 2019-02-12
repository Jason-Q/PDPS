package cn.xuan.pdps.utils;
import cn.xuan.pdps.model.Separator;
import cn.xuan.pdps.model.SocialNetwork;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class SocialNetworkReaderUtils {
    public static SocialNetwork constructSocialNetworkFromFile(File file) throws Exception {
        return constructSocialNetworkFromFile(file, null);
    }

    public static SocialNetwork constructSocialNetworkFromFile(File file, String pvalueFilePath) throws Exception {
        SocialNetwork network = new SocialNetwork();
        if (file.isFile()) {
            network = constructSocialNetworkFromFile(file.getAbsolutePath(), pvalueFilePath);
        } else if (file.isDirectory()) {
            network = constructSocialNetworkFromFiles(file.listFiles(), pvalueFilePath);
        }
        return network;
    }

    private static SocialNetwork constructSocialNetworkFromFile(String filePath, String pvalueFilePath) throws Exception {
        SocialNetwork network = new SocialNetwork();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line = reader.readLine();
        while (null != line) {
            String[] edge = line.split(Separator.fieSeparator);
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

    private static SocialNetwork constructSocialNetworkFromFiles(File[] files, String pvalueFilePath) throws Exception {
        SocialNetwork network = new SocialNetwork();
        BufferedReader reader = null;
        for (File file : files) {
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (null != line) {
                String[] edge = line.split(Separator.fieSeparator);
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
