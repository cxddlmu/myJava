package nio;

import com.google.common.base.Charsets;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by cuixiaodong on 2017/4/5.
 */
public class NIOServer implements Runnable {
    private Selector selector;
    ByteBuffer buffer = ByteBuffer.allocate(101);

    public void initServer(int port) throws IOException {
        // 获得一个ServerSocket通道
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        // 设置通道为非阻塞
        serverChannel.configureBlocking(false);
        // 将该通道对应的ServerSocket绑定到port端口
        serverChannel.socket().bind(new InetSocketAddress(port));
        // 获得一个通道管理器
        this.selector = Selector.open();
        //将通道管理器和该通道绑定，并为该通道注册SelectionKey.OP_ACCEPT事件,注册该事件后，
        //当该事件到达时，selector.select()会返回，如果该事件没到达selector.select()会一直阻塞。
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    /**
     * 采用轮询的方式监听selector上是否有需要处理的事件，如果有，则进行处理
     *
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void listen() throws IOException, InterruptedException {
        System.out.println("服务端启动成功！");
// 轮询访问selector
        while (true) {
//当注册的事件到达时，方法返回；否则,该方法会一直阻塞
            int select = selector.select();
            if (select <= 0) {
                continue;
            }
// 获得selector中选中的项的迭代器，选中的项为注册的事件
            Iterator ite = this.selector.selectedKeys().iterator();
            while (ite.hasNext()) {
                SelectionKey key = (SelectionKey) ite.next();
// 删除已选的key,以防重复处理
                ite.remove();
// 客户端请求连接事件
                if (key.isAcceptable()) {
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
// 获得和客户端连接的通道
                    SocketChannel channel = server.accept();
// 设置成非阻塞
                    channel.configureBlocking(false);
//在这里可以给客户端发送信息哦
                    //channel.write(ByteBuffer.wrap(new String("server:connected").getBytes(Charsets.UTF_8)));
//在和客户端连接成功之后，为了可以接收到客户端的信息，需要给通道设置读的权限。
                    channel.register(this.selector, SelectionKey.OP_READ);
//                    key.attach(new ProcessorSingleReactor());
// 获得了可读的事件
                } else if (key.isReadable()) {
                    read(key);
                } else if (key.isWritable()) {
                    //ByteBuffer sendbuffer = ByteBuffer.allocate(100);

//将缓冲区清空以备下次写入
                    buffer.clear();
// 返回为之创建此键的通道。
                    SocketChannel client = (SocketChannel) key.channel();
                    String sendText = "<<message from server>>";
//向缓冲区中输入数据
                    buffer.put(sendText.getBytes());
//将缓冲区各标志复位,因为向里面put了数据标志被改变要想从中读取数据发向服务器,就要复位
                    buffer.flip();
//输出到通道
                    client.write(buffer);
                    //System.out.println(" server send--：" + sendText);
                    client.register(selector, SelectionKey.OP_READ);
                }
            }
        }
    }

    /**
     * 处理读取客户端发来的信息的事件
     *
     * @paramkey
     * @throwsIOException
     */
    public void read(SelectionKey key) throws IOException, InterruptedException {
        buffer.clear();
//服务器可读取消息:得到事件发生的Socket通道
        SocketChannel channel = (SocketChannel) key.channel();
//创建读取的缓冲区
        //ByteBuffer buffer = ByteBuffer.allocate(100);
        channel.read(buffer);
        byte[] data = buffer.array();
        String msg = new String(data, Charsets.UTF_8).trim();
        System.out.println("server:receive " + msg);
        ByteBuffer outBuffer = ByteBuffer.wrap("server:send".getBytes(Charsets.UTF_8));
//        Thread.currentThread().sleep(10000L);
        //channel.write(outBuffer);//将消息回送给客户端
        channel.register(selector, SelectionKey.OP_WRITE);
        //ProcessorSingleReactor attachment = (ProcessorSingleReactor) key.attachment();
        //attachment.process(key);
    }


    public void run() {
        NIOServer server = new NIOServer();
        try {
            server.initServer(7777);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            server.listen();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
                new Thread(new NIOServer()).start();

    }
}
