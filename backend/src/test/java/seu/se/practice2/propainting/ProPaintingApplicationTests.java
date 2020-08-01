package seu.se.practice2.propainting;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import seu.se.practice2.propainting.component.cos.COSFileManager;
import seu.se.practice2.propainting.controller.AccountController;
import seu.se.practice2.propainting.model.dao.mapper.AccountMapper;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Path;

@SpringBootTest
class ProPaintingApplicationTests {

    @Autowired
    AccountController accountController;

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    COSFileManager cosFileManager;

    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket("172.18.0.3", 18085);
        BufferedWriter bufferedWriter =
            new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bufferedWriter.write("你是谁?$");
        bufferedWriter.flush();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String s = bufferedReader.readLine();
        System.out.println(s);
    }

    private static void success() throws IOException, InterruptedException {
        Path projectDir = FileSystems.getDefault().getPath(System.getProperty("user.dir"));
        Path pythonDir = projectDir.resolve("python").resolve("QAsystem");
        Path targetFilePath = pythonDir.resolve("test.py");
        String s = pythonDir.toString();
        s = s.replace("\\", "/");
        String question = "你是谁?";

        ProcessBuilder pb =
            new ProcessBuilder(
                "cmd.exe", "/c",
                "cd D:/Users/leimei/all/works/propainting/backend/python/QAsystem/",
                " & D:/Users/leimei/AppData/Local/Programs/Python/Python37/python test.py 你是谁"
            );
        pb.redirectErrorStream(true);
        Process process = pb.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), Charset.forName("GBK")));
        String line;
        StringBuffer colors = new StringBuffer();
        while ((line = reader.readLine()) != null)
            colors.append(line).append("\r\n");
        process.waitFor();
        System.out.println(colors);
    }

    @Test
    void contextLoads() {

//        File file = new File("I:\\cache\\ea-ws\\DemoJava\\conf\\1.txt");
//        InputStream in = new FileInputStream(file);
//        func.__call__(new PyFile(in));
    }

}
