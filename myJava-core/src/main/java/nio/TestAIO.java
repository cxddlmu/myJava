package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by cuixiaodong on 2017/3/30.
 */
public class TestAIO {
}

class AIOServer {
    public final static int PORT = 9888;
    private AsynchronousServerSocketChannel server;

    public AIOServer() throws IOException {
        server = AsynchronousServerSocketChannel.open().bind(
                new InetSocketAddress(PORT));
    }

    public void startWithFuture() throws InterruptedException,
            ExecutionException, TimeoutException {
        while (true) {// 循环接收客户端请求
            Future<AsynchronousSocketChannel> future = server.accept();
            AsynchronousSocketChannel socket = future.get();// get() 是为了确保 accept 到一个连接
            handleWithFuture(socket);
        }
    }

    public void handleWithFuture(AsynchronousSocketChannel channel) throws InterruptedException, ExecutionException, TimeoutException {
        ByteBuffer readBuf = ByteBuffer.allocate(2);
        readBuf.clear();

        while (true) {// 一次可能读不完
            //get 是为了确保 read 完成，超时时间可以有效避免DOS攻击，如果客户端一直不发送数据，则进行超时处理
            Integer integer = channel.read(readBuf).get(10, TimeUnit.SECONDS);
            System.out.println("read: " + integer);
            if (integer == -1) {
                break;
            }
            readBuf.flip();
            System.out.println("received: " + Charset.forName("UTF-8").decode(readBuf));
            readBuf.clear();
        }
    }

    public void startWithCompletionHandler() throws InterruptedException,
            ExecutionException, TimeoutException {
        server.accept(null,
                      new CompletionHandler<AsynchronousSocketChannel, Object>() {
                          public void completed(AsynchronousSocketChannel result, Object attachment) {
//                              server.accept(null, this);// 再此接收客户端连接
//                              handleWithCompletionHandler(result);
                              server.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {//2.注册AIO异步事件Handler
                                  final ByteBuffer buffer = ByteBuffer.allocate(1024);
                                  //3.用户态IO就绪完成事件
                                  public void completed(AsynchronousSocketChannel result, Object attachment) {
                                      System.out.println(Thread.currentThread().getName());
                                      try {
                                          Thread.currentThread().sleep(5000);
                                      } catch (InterruptedException e) {
                                          e.printStackTrace();
                                      }
                                      System.out.println("start");
                                      try {

                                          //4.用户态进行IO操作，将用户态缓冲区的数据用于业务逻辑

                                          buffer.clear();

                                          result.read(buffer).get(100, TimeUnit.SECONDS);

                                          buffer.flip();

                                          System.out.println("received message: " + new String(buffer.array()));

                                      } catch (InterruptedException | ExecutionException e) {

                                          System.out.println(e.toString());

                                      } catch (TimeoutException e) {

                                          e.printStackTrace();

                                      } finally {

                                          try {

                                              result.close();

                                              server.accept(null, this);

                                          } catch (Exception e) {

                                              System.out.println(e.toString());

                                          }

                                      }

                                      System.out.println("end");

                                  }

                                  public void failed(Throwable exc, Object attachment) {

                                      System.out.println("failed: " + exc);

                                  }

                              });

                              // 主线程继续自己的行为

                              while (true) {

                                  System.out.println("main thread");

                                  try {
                                      Thread.sleep(1000);
                                  } catch (InterruptedException e) {
                                      e.printStackTrace();
                                  }

                              }
                          }

                          @Override
                          public void failed(Throwable exc, Object attachment) {
                              exc.printStackTrace();
                          }
                      });
    }

    public void handleWithCompletionHandler(final AsynchronousSocketChannel channel) {
        try {
            final ByteBuffer buffer = ByteBuffer.allocate(4);
            final long timeout = 10L;
            channel.read(buffer, timeout, TimeUnit.SECONDS, null, new CompletionHandler<Integer, Object>() {
                @Override
                public void completed(Integer result, Object attachment) {
                    System.out.println("read:" + result);
                    if (result == -1) {
                        try {
                            channel.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                    buffer.flip();
                    System.out.println("received message:" + Charset.forName("UTF-8").decode(buffer));
                    buffer.clear();
                    channel.read(buffer, timeout, TimeUnit.SECONDS, null, this);
                }

                @Override
                public void failed(Throwable exc, Object attachment) {
                    exc.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws Exception {
//        new AIOServer().startWithFuture();
        new AIOServer().startWithCompletionHandler();
        Thread.sleep(100000);
    }
}

class AIOClient {

    public static void main(String... args) throws Exception {
        AsynchronousSocketChannel client = AsynchronousSocketChannel.open();
        client.connect(new InetSocketAddress("localhost", 9888)).get();
        client.write(ByteBuffer.wrap("123456789".getBytes()));
        Thread.sleep(1111111);
    }
}
