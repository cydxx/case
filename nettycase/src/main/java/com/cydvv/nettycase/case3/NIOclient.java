package com.cydvv.nettycase.case3;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SocketChannel;

public class NIOclient {
    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);
        
        if (!socketChannel.connect(inetSocketAddress)){
            while (!socketChannel.finishConnect()){
                System.out.println("因为连接需要时间，客户端不会阻塞");
            }
        }
        String ser = "你好啊！在否。";
        ByteBuffer wrap = ByteBuffer.wrap(ser.getBytes());
        socketChannel.write(wrap);
        System.in.read();
    }
}
