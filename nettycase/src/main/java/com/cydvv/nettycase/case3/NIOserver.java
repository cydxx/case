package com.cydvv.nettycase.case3;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOserver {
    public static void main(String[] args) throws Exception{

        ServerSocketChannel socketChannel = ServerSocketChannel.open().bind(new InetSocketAddress(6666));

        Selector selector = Selector.open();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector,SelectionKey.OP_ACCEPT);

        while (true){
            if (selector.select(1000) == 0) {
                System.out.println("wait 1s-----");
                continue;
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey next = iterator.next();
                if(next.isAcceptable()){
                    SocketChannel accept = socketChannel.accept();
                    accept.configureBlocking(false);
                    accept.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                if (next.isReadable()){
                    SocketChannel socketChannel1 = (SocketChannel) next.channel();
                    ByteBuffer byteBuffer = (ByteBuffer) next.attachment();
                    socketChannel1.read(byteBuffer);
                    System.out.println("from客户端="+new String(byteBuffer.array()));
                }
                iterator.remove();
            }
        }

    }
}
