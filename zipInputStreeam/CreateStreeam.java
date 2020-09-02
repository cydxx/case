

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


@Slf4j
public class CreateStreeam {

    /**
     * upload
     */
    @Autowired(required = false)
    private JssUtils jssUtils;

    /**
     * 文件转换
     * @param file
     * @return
     * @throws Exception
     */
    public static File multipartFileToFile(MultipartFile file) throws Exception {

        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    /**
     * 获取流文件
     */
    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除本地临时文件
     *
     * @param file
     */
    public static void delteTempFile(File file) {
        if (file != null) {
            File del = new File(file.toURI());
            del.delete();
        }

    }

    /**
     * 上传
     * @param file
     */
    public void uploadFiles(MultipartFile file){
        try {
            String originalFilename = file.getOriginalFilename();
            int i = originalFilename.lastIndexOf(".");
            File file1 = multipartFileToFile(file);
            unZipFile(file1, originalFilename.substring(0, i));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 上传文件
     *
     * @param zipfile
     * @param originalFilename
     */
    public void unZipFile(File zipfile, String originalFilename) {
        try {
            // Open the ZIP file
            ZipFile zf = new ZipFile(zipfile);
            for (Enumeration entries = zf.entries(); entries.hasMoreElements(); ) {
                // Get the entry name
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String zipEntryName = entry.getName();
                if (zipEntryName.startsWith(originalFilename) && entry.getSize() > 0) {
                    log.info("zipEntryName={}", zipEntryName);
                    log.info("entry.getSize={}", entry.getSize());
                    byte[] bytes=new byte[1024];
                    InputStream inputStream=zf.getInputStream(entry);//得到文件的InputStream对象。接下来就可以得到文件内容
                    BufferedInputStream bufferedInputStream=new BufferedInputStream(inputStream);
                    ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                    int num=-1;
                    while((num=bufferedInputStream.read(bytes,0,bytes.length))>-1){
                        byteArrayOutputStream.write(bytes,0,num);
                    }
                    byte[] bytes_ =byteArrayOutputStream.toByteArray();
                    int i = zipEntryName.lastIndexOf("/");
                    String fileName = zipEntryName.substring(i + 1);
                    InputStream byteArrayInputStream = new ByteArrayInputStream(bytes_);
                    String upload = utils.upload(fileName, byteArrayInputStream, entry.getSize());
                    log.info("upload-url={}", upload);
                    //关流
                    inputStream.close();
                    bufferedInputStream.close();
                    byteArrayOutputStream.close();
                    byteArrayInputStream.close();
                }
            }
        } catch (Exception e) {
            log.error("ZipUtil unZipFiles exception:" + e);
        }
    }
}
