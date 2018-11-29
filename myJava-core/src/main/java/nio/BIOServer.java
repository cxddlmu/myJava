package nio;

import com.google.common.base.Charsets;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by cuixiaodong on 2017/4/5.
 */
public class BIOServer implements Runnable {
    private void init() throws IOException {
        ServerSocket servSocket = new ServerSocket(7777);
        int recvMsgSize = 0;
        byte[] recvBuf = new byte[102411];
        while (true) {
            Socket clntSocket = servSocket.accept();// 该方法会阻塞
            clntSocket.setSoTimeout(3000);
            System.out.println("Handling client at " + 7777);
            InputStream in = clntSocket.getInputStream();
            OutputStream out = clntSocket.getOutputStream();
            while ((recvMsgSize = in.read(recvBuf)) != -1) {
                //out.write(recvBuf, 0, recvMsgSize);
                System.out.println("server:receive " + new String(recvBuf, Charsets.UTF_8));
            }
            clntSocket.close();
        }
    }

    public void run() {
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
