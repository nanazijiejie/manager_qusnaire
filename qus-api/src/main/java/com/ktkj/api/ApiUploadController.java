package com.ktkj.api;

import com.ktkj.annotation.IgnoreAuth;
import com.ktkj.oss.OSSFactory;
import com.ktkj.util.ApiBaseAction;
import com.ktkj.util.ImageUtils;
import com.ktkj.utils.RRException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.io.*;
import org.apache.commons.fileupload.disk.*;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;

/**
 * 作者: @author Harmon <br>
 * 时间: 2017-09-08 13:20<br>
 * 描述: ApiUploadController <br>
 */
@Api(tags = "上传")
@RestController
@RequestMapping("/api/upload")
public class ApiUploadController extends ApiBaseAction {

    /**
     * 上传文件
     */
	@ApiOperation(value = "上传文件")
    @IgnoreAuth
    @PostMapping("/upload")
    public Object upload(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new RRException("上传文件不能为空");
        }
        //上传文件
        String url = OSSFactory.build().upload(file);
        return toResponsSuccess(url);
    }
    @IgnoreAuth
    @RequestMapping("/upload3")
    @ResponseBody
    public Object upload3(HttpServletRequest request) throws Exception {
	    String imgPath= "/data/images/tianan/";
        //String imgPath= "/Users/luonana/IdeaProjects/ring/ring-shop/src/main/webapp/tianan/";
        String fileName = "tianantmp_"+(new Date()).getTime()+".png";
        String base64 = request.getParameter("image");
        ImageUtils.base64ToFile(base64,imgPath+fileName);
        //ImageUtils.changePngToJpeg("/Users/luonana/Downloads/tmp2.png","/Users/luonana/Downloads/tmp2.jpg");
        ImageUtils.changeSize(258,258,imgPath+fileName);
        String finalImgName = ImageUtils.exportImg2(imgPath+"WechatIMG1017.png",imgPath+fileName,imgPath,0,0);
        //客户上传的文件删除
        File file = new File(imgPath+fileName);
        if (file.exists()) {
            file.delete();
        }
        //return toResponsSuccess("https://images.jixuxiang.com/images/"+finalImgName);
        return toResponsSuccess("https://images.jixuxiang.com/images/"+finalImgName);
    }

    @IgnoreAuth
    @RequestMapping("/genShareImg")
    @ResponseBody
    public Object genShareImg(HttpServletRequest request) throws Exception {
        //String imgPath= "/data/images/";
        String imgPath= "/Users/luonana/IdeaProjects/ring/ring-shop/src/main/webapp/tianan/";
        String fileName = request.getParameter("image");
        String firstFileName = "photos.png";
        ImageUtils.changeSize(258,350,imgPath+firstFileName);
        String finalImgName = ImageUtils.exportImg2(imgPath+fileName,imgPath+firstFileName,imgPath,0,0);
        //return toResponsSuccess("https://images.jixuxiang.com/images/"+finalImgName);
        return toResponsSuccess("http://192.168.0.103:8080/ring_framework_war_exploded/tianan/"+finalImgName);
    }

    @IgnoreAuth
    @PostMapping("/upload2")
    public Map<String, String> reciveImageAndParams(HttpServletRequest request) throws Exception {

        Map<String, String> uri = new HashMap<String, String>();

        String tempPathDir = "";
        File tempPathDirFile = new File(tempPathDir);

        // 创建工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置缓冲区大小，这里是400kb
        factory.setSizeThreshold(4096 * 100);
        // 设置缓冲区目录
        factory.setRepository(tempPathDirFile);
        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);
        // 设置上传文件的大小 12M
        upload.setSizeMax(4194304 * 3);
        // 创建解析器
        // 得到所有的文件
        List<FileItem> items = upload.parseRequest(request);
        Iterator<FileItem> i = items.iterator();
        //图片地址拼接
        StringBuffer buf=new StringBuffer();
        String img_urls="";
        int j=0;//防止重名
        while (i.hasNext()) {
            FileItem fi = i.next();
            // false表示文件 否则字段
            if (!fi.isFormField()) {
                String fileName = fi.getName();
                if (fileName != null) {
                    // 这里加一个限制，如果不是图片格式，则提示错误. (gif,jpg,jpeg,bmp,png)
                    String suffixName = FilenameUtils.getExtension(fileName);
                    if ("jpg".equalsIgnoreCase(suffixName) || "jpeg".equalsIgnoreCase(suffixName)
                            || "png".equalsIgnoreCase(suffixName) || "gif".equalsIgnoreCase(suffixName)) {

                        j++;
                        String imageName = System.currentTimeMillis()+j+ "." + suffixName;
                        //String imgPath = "e:" + imageName;// 本机
                        //ConfUtil cf = new ConfUtil();
                        String imgPath = "" + imageName;//服务器

                        //图片地址拼接
                        String img_url="" + imageName;
                        img_urls=buf.append(img_url).append(",").toString();
                        // 定义图片流
                        InputStream fin = fi.getInputStream();
                        // 定义图片输出流
                        FileOutputStream fout = new FileOutputStream(imgPath);
                        // 写文件
                        byte[] b = new byte[1024];
                        int length = 0;
                        while ((length = fin.read(b)) > 0) {
                            fout.write(b, 0, length);
                        }
                        // 关闭数据流
                        fin.close();
                        fout.close();

                    } else {
                        // throw new ProcureException("文件格式不正确");
                    }
                }
            } else {
                fi.getString("UTF-8");
                uri.put(new String(fi.getFieldName()),
                        new String(fi.getString()));
            }
        }
        if(img_urls!="") {

            img_urls=img_urls.substring(0,img_urls.length()-1);
        }
        uri.put("img_urls", img_urls);
        return uri;

    }
}