
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import javax.swing.text.html.parser.Entity;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class spider {
    private static final String R_URL[] = {"https://www.colorhub.me/search?tag=%E5%8A%A8%E7%89%A9"
            , "https://www.colorhub.me/search?tag=%E5%8A%A8%E7%89%A9&page=4","https://www.colorhub.me/search?tag=%E5%8A%A8%E7%89%A9&page=7"
    ,"https://www.colorhub.me/search?tag=%E6%B0%B4%E6%9E%9C","https://www.colorhub.me/search?tag=%E5%BB%BA%E7%AD%91"
    ,"https://www.colorhub.me/search?tag=%E5%9F%8E%E5%B8%82","https://www.colorhub.me/search?tag=%E9%A3%8E%E6%99%AF",
      "https://www.colorhub.me/search?tag=%E4%B9%A6%E6%9F%9C","https://www.colorhub.me/search?tag=%E6%89%8B%E5%B7%A5%E8%89%BA",
            "https://www.colorhub.me/search?tag=%E9%A3%8E%E6%99%AF&page=5","https://www.colorhub.me/search?tag=%E9%A3%8E%E6%99%AF&page=8",
            "https://www.colorhub.me/search?tag=%E8%8A%B1"
    };
    private int width=0;
    private int height=0;
    @Test
    public void requestData() throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        int length = R_URL.length;
        int i=0;
        while(i<length) {
            HttpGet httpGet = new HttpGet(R_URL[i]);
            httpGet.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            CloseableHttpResponse response = null;
            response = httpClient.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            System.out.println(statusLine.getStatusCode());
            HttpEntity httpEntity = response.getEntity();
            String content = EntityUtils.toString(httpEntity, "utf-8");
            //   System.out.println(content);
            parseHtml(content);
            i++;
        }
    }

    private void parseHtml(String content) throws IOException {
         Document document= Jsoup.parse(content);
         Elements elements= document.select("div.card");
  //       System.out.println(elements);
         for(Element item:elements)
         {
             Elements links = item.getElementsByTag("a");
             String url = links.attr("href");
      //       System.out.println(url);
             parseAnother(url);

         }
    }

    private  void parseAnother(String url) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet=new HttpGet(url);
        httpGet.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        CloseableHttpResponse response =null;
        response= httpClient.execute(httpGet);
        StatusLine statusLine= response.getStatusLine();
        System.out.println(statusLine.getStatusCode());
        HttpEntity httpEntity=response.getEntity();
        String content = EntityUtils.toString(httpEntity,"utf-8");
        System.out.println("某一页面");
        Document document= Jsoup.parse(content);
        Elements elements= document.select("div.card");
//        System.out.println(elements);
        for(Element item:elements)
        {
            Elements links = item.getElementsByTag("img");
        //    Elements img =
            String title = links.attr("title");
            String urlPicture = links.attr("src");
            if(title==null||!urlPicture.startsWith("//"))
                continue;
            urlPicture ="https:"+urlPicture;
            System.out.println(urlPicture+title);

            getImageInfoByUrl(urlPicture);
            title.replaceAll(" ", ""); //去掉所有空格，包括首尾、中间

            StringBuilder stringBuilder= new StringBuilder();
            stringBuilder.append(title).append("\t").append(urlPicture).append("\t").append(width)
                    .append("\t").append(height);
            writeFile(stringBuilder);

        }

    }
    private void writeFile(StringBuilder strings) throws IOException {
        String path = "F:\\小学期\\spider\\src\\main\\resources\\picture.txt";
        File file = new File(path);
        if(!file.exists()){
            file.getParentFile().mkdirs();
        }
        file.createNewFile();
        // write
        FileWriter fw = new FileWriter(file, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(strings+"\r\n");
        bw.flush();
        bw.close();
        fw.close();

        // read
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String str = br.readLine();
    }

    @Test
    public void testUrl() throws IOException {
        System.out.println("what");
        getImageInfoByUrl("https://cdn.colorhub.me/imgsrv/243daba723a0f403d6b65ab70a0cad32d096d425");
    }
    public void getImageInfoByUrl(String urlStr) throws IOException {
            URL url = new URL(urlStr);
            URLConnection urlConnection = url.openConnection();
            int length = urlConnection.getContentLength();
            System.out.print("length: " + length);
            if(length == -1){
                System.out.print("image not exist");
            }else{
                BufferedImage sourceImg = ImageIO.read(url);
                System.out.print("image width:" + sourceImg.getWidth());
                System.out.print("image height:" + sourceImg.getHeight());
                width=sourceImg.getWidth();
                height=sourceImg.getHeight();
            }

    }



    }


