package ru.kosarev.cloud6.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class NioServer {

    private final ServerSocketChannel server;
    private final Selector selector;

    public NioServer() throws IOException {
        server = ServerSocketChannel.open();
        selector = Selector.open();
        server.bind(new InetSocketAddress(8190));
        server.configureBlocking(false); //асинхронный режим чтения
        server.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void start() throws IOException {
        while (server.isOpen()){
            selector.select();

            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                if (key.isAcceptable()){
                    handAccept();

                }
                if (key.isReadable()){
                    handleRead(key);
                }
                iterator.remove();

            }

        }
    }

    private void handleRead(SelectionKey key) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(1024);
         SocketChannel channel = (SocketChannel) key.channel();
         StringBuilder s = new StringBuilder();
        while (channel.isOpen()){
            int read = channel.read(buf);
            if(read<0){
                channel.close();
                return;
            }
            if (read == 0){
                break;
            }
                buf.flip();
                while (buf.hasRemaining()){
                s.append((char) buf.get());
                }
                buf.clear();
        }
        s.append("-> ");
        byte[] message = s.toString().getBytes(StandardCharsets.UTF_8);

        for(SelectionKey selectionKey : selector.keys()){
            if(selectionKey.isValid() && selectionKey.channel() instanceof SocketChannel sc){
                channel.write(ByteBuffer.wrap(message));


            }
        }
    }

    private  void handAccept() throws IOException {
         SocketChannel channal = server.accept();
         channal.configureBlocking(false);
         channal.register(selector,SelectionKey.OP_READ);
         channal.write(ByteBuffer.wrap("Welcome in my terminal!\n".getBytes(StandardCharsets.UTF_8)));
     }

}
