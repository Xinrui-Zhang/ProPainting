package seu.se.practice2.propainting.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import seu.se.practice2.propainting.component.cos.COSFileManager;
import seu.se.practice2.propainting.config.thread_pool.ImageTransRecrusiveTask;
import seu.se.practice2.propainting.model.dao.mapper.PictureMapper;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

/**
 * author: leimei date: 2020/7/23 22:15
 */
@Component
public class URLUtil {

    @Autowired
    ForkJoinPool forkJoinPool;

    @Autowired
    PictureMapper pictureMapper;

    @Autowired
    COSFileManager fileManager;

    public static final String cosPrefix = "https://propainting-1301790570.cos.ap-nanjing.myqcloud.com/";

    public void cosURLPrefix(List<Map<String, Object>> maps) {
        maps.forEach(map -> {
            map.forEach((k, v) -> {
                String sv = "";
                if (k.toLowerCase().equals("url")) {
                    if ((sv = (String) v).startsWith("picture")) {
                        map.put(k, cosPrefix + sv);
                    }
                }
            });
        });
    }

    public static String cosURLPrefix(String url) {
        return cosPrefix + url;
    }
}
