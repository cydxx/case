
    /**
     * 查询监播回放
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/monitor/uploadImgs", method = RequestMethod.POST)
    public Object uploadImg(
            @ApiParam(value = "file") @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        String filename = file.getOriginalFilename();
        log.info("fileName={}",filename);
        int i = filename.lastIndexOf(".");
        String startName = filename.substring(0,i);
        log.info("startName={}",startName);
        String path = "/export/data/zip/";
        File dir = new File(path);
        dir.mkdirs();
        String filePath = "/export/data/test/";
        File fileDir = new File(filePath);
        fileDir.mkdirs();
        File saveFile = new File(fileDir, filename);
        List<String>list = new ArrayList<>();
        try {
            file.transferTo(saveFile);
            String newFilePath = filePath + filename;
            File zipFile = new File(newFilePath);
            unZipFiles(zipFile, path,list,startName);//解压文件，获取文件路径

            if (!CollectionUtils.isEmpty(list)){
                for (String s : list) {
                    File f = new File(s);
                    InputStream in = new BufferedInputStream(new FileInputStream(f));
                    long length = f.length();
                    log.info("length={}",length);
                    log.info("name={}",f.getName());
                    String upload = utils.upload(f.getName(),in,length);
                    log.info("upload-url={}",upload);
                }
            }
            System.out.println(JSON.toJSONString(list));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("解压执行失败");
        }
        //程序结束时，删除临时文件
        deleteFiles(filePath);//删除压缩包文件夹
        deleteFiles(path);//删除解压文件夹**

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("ret",list);
        return jsonMap;


    }

    /**
     * zip解压
     * @param srcFile        zip源文件
     * @param destDirPath     解压后的目标文件夹
     * @throws RuntimeException 解压失败会抛出运行时异常
     */
    public static void unZipFiles(File srcFile, String destDirPath,List<String> list,String originalFilename) throws RuntimeException {
        long start = System.currentTimeMillis();
        // 判断源文件是否存在
        if (!srcFile.exists()) {
            throw new RuntimeException(srcFile.getPath() + "所指文件不存在");
        }
        // 开始解压
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(srcFile);
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                System.out.println("解压" + entry.getName());
                    // 如果是文件夹，就创建个文件夹
                    if (entry.isDirectory()) {
                        String dirPath = destDirPath + "/" + entry.getName();
                        File dir = new File(dirPath);
                        dir.mkdirs();
                    } else {
                        // 如果是文件，就先创建一个文件，然后用io流把内容copy过去
                        File targetFile = new File(destDirPath + "/" + entry.getName());
                        // 保证这个文件的父文件夹必须要存在

                        if (!targetFile.getParentFile().exists()) {

                        }
                        list.add(targetFile.getPath());
                        targetFile.createNewFile();
                        // 将压缩文件内容写入到这个文件中
                        InputStream is = zipFile.getInputStream(entry);
                        FileOutputStream fos = new FileOutputStream(targetFile);
                        int len;
                        byte[] buf = new byte[1024];
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                        }
                        // 关流顺序，先打开的后关闭
                        fos.close();
                        is.close();
                    }
                }

            long end = System.currentTimeMillis();
            System.out.println("解压完成，耗时：" + (end - start) +" ms");
        } catch (Exception e) {
            throw new RuntimeException("unzip error from ZipUtils", e);
        } finally {
            if(zipFile != null){
                try {
                    zipFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param filePath
     * 临时文件的删除
     * 删除文件夹里面子目录
     * 再删除文件夹
     */
    public void deleteFiles(String filePath) {
        File file = new File(filePath);
        if ((!file.exists()) || (!file.isDirectory())) {
            System.out.println("file not exist");
            return;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (filePath.endsWith(File.separator)) {
                temp = new File(filePath + tempList[i]);
            }
            else {
                temp = new File(filePath + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                this.deleteFiles(filePath + "/" + tempList[i]);
            }
        }
        // 空文件的删除
        file.delete();
    }

