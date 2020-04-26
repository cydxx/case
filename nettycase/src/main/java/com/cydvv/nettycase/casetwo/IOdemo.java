package com.cydvv.nettycase.casetwo;

public class IOdemo {
  public static void main(String[] args) throws Exception {
        FileChannelDemo e = new FileChannelDemo();
        //e.outStream();
        //e.inStream();
        //e.cpStream();
        //e.cpJpg();
//        e.bufferTest();
        e.mappedBufferTest();
    }


    public void outStream() throws Exception{
        String str = "hello world !!!!你好！";
        FileOutputStream fileOutputStream = new FileOutputStream("1.txt");
        FileChannel channel = fileOutputStream.getChannel();

        byte[] bytes = str.getBytes();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put(bytes);
        byteBuffer.flip();

        int write = channel.write(byteBuffer);
        System.out.println(write);
        fileOutputStream.close();

    }

    public void inStream() throws Exception{
        FileInputStream fileInputStream = new FileInputStream("1.txt");
        FileChannel channel = fileInputStream.getChannel();

        System.out.println(fileInputStream.available());
        ByteBuffer byteBuffer = ByteBuffer.allocate(fileInputStream.available());
        channel.read(byteBuffer);
        byteBuffer.flip();

        System.out.println(new String(byteBuffer.array()));
        fileInputStream.close();



    }

    public void cpStream() throws Exception{
        FileInputStream fileInputStream = new FileInputStream("1.txt");
        FileChannel channel = fileInputStream.getChannel();
        FileOutputStream fileOutputStream = new FileOutputStream("2.txt");
        FileChannel channel1 = fileOutputStream.getChannel();
        ByteBuffer allocate = ByteBuffer.allocate(fileInputStream.available());
        channel.read(allocate);
        allocate.flip();
        channel1.write(allocate);
        fileInputStream.close();
        fileOutputStream.close();

    }

    public void cpJpg() throws Exception{
        FileInputStream fileInputStream = new FileInputStream("a.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream("b2.jpg");
        FileChannel channel = fileInputStream.getChannel();
        FileChannel channel1 = fileOutputStream.getChannel();
        channel1.transferFrom(channel,0,channel.size());
        fileInputStream.close();
        fileOutputStream.close();

    }

    public void bufferTest() {
        ByteBuffer allocate = ByteBuffer.allocate(64);
        allocate.putInt(100);
        allocate.putLong(9);
        allocate.putChar('成');
        allocate.flip();
        System.out.println(allocate.getInt());
        System.out.println(allocate.getLong());
        System.out.println(allocate.getChar());
        System.out.println(System.currentTimeMillis());
    }
    public void mappedBufferTest() throws Exception {


        RandomAccessFile randomAccessFile = new RandomAccessFile("1.txt","rw");
        FileChannel channel = randomAccessFile.getChannel();
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 30);
        map.put(0,(byte) 'H');
        map.put(28,(byte) '9');
        randomAccessFile.close();
    }
}
