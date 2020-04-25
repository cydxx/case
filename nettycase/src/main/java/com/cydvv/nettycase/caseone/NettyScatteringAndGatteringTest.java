package com.cydvv.nettycase.caseone;

import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public class NettyScatteringAndGatteringTest {
    public static void main(String[] args) throws Exception {


        ServerSocketChannel socketChannel = ServerSocketChannel.open().bind(new InetSocketAddress(7000));

        SocketChannel accept = socketChannel.accept();

        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        int messlen = 8;

        while (true){
            int readByte = 0;
            while (readByte < messlen){
                long read = accept.read(byteBuffers);
                readByte += read;
                System.out.println("readByte="+readByte);
                Arrays.asList(byteBuffers).stream().map(buffer -> "position="+buffer.position()+",limit="+buffer.limit()).forEach(System.out::println);
            }
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.flip());

            long writebyte = 0;
            while (writebyte < messlen){
                long write = accept.write(byteBuffers);
                writebyte += write;
            }
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.clear());
            System.out.println("read="+readByte+"write="+writebyte);
        }



    }
}
