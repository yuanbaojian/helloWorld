package com.ybj.crawler.utils.Crawler.FIleDownload;


import com.ybj.crawler.utils.FileNameUtils;
import com.ybj.crawler.utils.FileUtils;
import com.ybj.crawler.utils.NetWorkUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @Author downloadPexels
 * @Description //TODO $
 * @Date $ $
 * @Param $
 * @return $
 **/
public class downloadPexels {
    public static  String pexeclURL="https://www.pexels.com/zh-tw/";
    public static  String baiduURL="https://www.baidu.com/";
    public static  String pexeclSelector="img[class=photo-item__img]";
    public static  String pexeclSelectorAttr="srcset";
    public static  String pexeclDescPrefix="G:\\clawer\\pexel\\";

    public static  String pbudejieURL="http://www.budejie.com/hot/2";
    public static  String budejieSelector="img[class=lazy]";
    public static  String budejieSelectorAttr="data-original";
    public static  String budejieDescPrefix="C:\\Users\\yuanbaojian\\Desktop\\desc\\budejie\\hot\\";


    public static String nineOnePornURL = "http://198.255.82.91/index.php";
    public static String nineOnePornSelector = "a[target=blank]";
    public static String nineOnePornSelectorAttr = "href";
    public static String nineOnePornDescPrefix = "C:\\Users\\yuanbaojian\\Desktop\\desc\\nineOne\\video\\";

    public static  String googlelURL="https://www.google.com/";
    public static Integer number=0;


    @Test
    public void testIOUtils() throws IOException {
        String path = pexeclURL;
        String pageContent = NetWorkUtils.getPageContent(pexeclURL, "POST");
        Document doc= Jsoup.parse(pageContent);
        Elements elements = doc.select(pexeclSelector);
        for (int i = 0; i <elements.size(); i++) {
            String photoURL = elements.get(i).attr(pexeclSelectorAttr);
            download(photoURL,pexeclDescPrefix);
        }
    }

    private void download(String photoURL, String pexeclDescPrefix) {
        String fileName= FileNameUtils.getName(photoURL);
        File picutreFile = new File(pexeclDescPrefix + fileName);
        try {
            URL url2=new URL(photoURL);
            URLConnection conn = url2.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:31.0) Gecko/20100101 Firefox/31.0");
            conn.connect();
            FileUtils.copyInputStreamToFile(conn.getInputStream(), picutreFile);
            picutreFile.createNewFile();
            number++;
            System.out.println("第 "+ number + "项----" + photoURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
