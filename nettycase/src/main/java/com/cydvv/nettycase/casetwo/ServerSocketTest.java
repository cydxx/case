public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        try {
            System.out.println("等待连接");
            ServerSocket serverSocket = new ServerSocket(8888);
            while (true) {
                Socket accept = serverSocket.accept();
                System.out.println("已连接"+Thread.currentThread().getId()+"--->"+Thread.currentThread().getName());
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        handle(accept);
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void handle(Socket accept ){
        try {
            byte[] bytes = new byte[1024];

            InputStream inputStream = accept.getInputStream();

            while (true){
                int read = inputStream.read(bytes);
                if (read!=-1){
                    System.out.println(new String(bytes));
                }else {
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                accept.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
