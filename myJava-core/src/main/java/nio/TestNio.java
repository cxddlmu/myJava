package nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by cuixiaodong on 2017/3/6.
 */
public class TestNio {
    public static void main(String[] args) throws IOException {

//        new Thread(new BioServer()).start();
//        new Thread(new NIOServer()).start();
//        new Thread(new NIOClient()).start();
//        new Thread(new NIOClient()).start();
    }

}







class ProcessorSingleReactor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessorSingleReactor.class);
    private static final ExecutorService service = Executors.newFixedThreadPool(16);

    public void process(SelectionKey selectionKey) {
        service.submit(() -> {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            int count = socketChannel.read(buffer);
            if (count < 0) {
                socketChannel.close();
                selectionKey.cancel();
                LOGGER.info("{}\t Read ended", socketChannel);
                return null;
            } else if (count == 0) {
                return null;
            }
            LOGGER.info("{}\t Read message {}", socketChannel, new String(buffer.array()));
            return null;
        });
    }
}

class ProcessorMultiReactor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessorMultiReactor.class);
    private static final ExecutorService service =
            Executors.newFixedThreadPool(2 * Runtime.getRuntime().availableProcessors());
    private Selector selector;

    public ProcessorMultiReactor() throws IOException {
        this.selector = SelectorProvider.provider().openSelector();
        start();
    }

    public void addChannel(SocketChannel socketChannel) throws ClosedChannelException {
        socketChannel.register(this.selector, SelectionKey.OP_READ);
    }

    public void start() {
        service.submit(() -> {
            while (true) {
                if (selector.selectNow() <= 0) {
                    continue;
                }
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isReadable()) {
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        int count = socketChannel.read(buffer);
                        if (count < 0) {
                            socketChannel.close();
                            key.cancel();
                            LOGGER.info("{}\t Read ended", socketChannel);
                            continue;
                        } else if (count == 0) {
                            LOGGER.info("{}\t Message size is 0", socketChannel);
                            continue;
                        } else {
                            LOGGER.info("{}\t Read message {}", socketChannel, new String(buffer.array()));
                        }
                    }
                }
            }
        });
    }
}
