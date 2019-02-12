package cn.xuan.pdps;

import cn.xuan.pdps.algo.pdps.PDPS;
import cn.xuan.pdps.model.SocialNetwork;
import cn.xuan.pdps.utils.SocialNetworkReaderUtils;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        File file = new File("data/");
        SocialNetwork network = SocialNetworkReaderUtils.constructSocialNetworkFromFile(file);

        PDPS pdps = new PDPS(network);
        pdps.run(7, 0.8);
        System.out.println();
    }
}
