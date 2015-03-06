package com.cos.web.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.StringUtils;
import com.cos.service.CosConnectionService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Joseph on 3/4/2015.
 */
@Controller
@RequestMapping(value = "/cos")
public class CosConnectionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CosConnectionController.class);
    @Autowired
    CosConnectionService cosConnectionService;

    @RequestMapping(value = "/entry", method = RequestMethod.POST)
    public String connect(HttpSession httpSession, String host, String accessKey, String secretKey) {
        AmazonS3 conn = cosConnectionService.connect(host, accessKey, secretKey);
        httpSession.setAttribute("connection", conn);
        return "redirect:buckets";
    }

    @RequestMapping(value = "/buckets", method = RequestMethod.GET)
    public String buckets(HttpSession httpSession, Model model) {
        AmazonS3 conn = (AmazonS3)httpSession.getAttribute("connection");
        List<Bucket> buckets = conn.listBuckets();
        for (Bucket bucket : buckets) {
            System.out.println(bucket.getName() + "\t" +
                    StringUtils.fromDate(bucket.getCreationDate()));
        }
        model.addAttribute("buckets", buckets);
        return "listBuckets";
    }

    @RequestMapping(value = "/bucket/{bucketName}/objects", method = RequestMethod.GET)
    public String objects(HttpSession httpSession, Model model, @PathVariable(value="bucketName") String bucketName) {
        AmazonS3 conn = (AmazonS3)httpSession.getAttribute("connection");
        ObjectListing objectListing = conn.listObjects(bucketName);
        List<S3ObjectSummary> objectSummary = new LinkedList<S3ObjectSummary>();
        do {
            objectSummary.addAll(objectListing.getObjectSummaries());
            objectListing = conn.listNextBatchOfObjects(objectListing);
        } while (objectListing.isTruncated());
        model.addAttribute("objects", objectSummary);
        return "listObjects";
    }

    class UploadFile implements Serializable {
        private static final long serialVersionUID = 1L;
        private int id;
        private String realName;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @RequestMapping(value = "/bucket/{bucketName}/object", method = RequestMethod.POST)
    public String uploadObjects(MultipartHttpServletRequest request, @PathVariable(value="bucketName") String bucketName) {
        List<UploadFile> uploadFiles = new ArrayList<UploadFile>();
        List<MultipartFile> files = request.getFiles("file");
        String dir = request.getSession().getServletContext().getRealPath("/") + "upload";
        for (int i = 0; i < files.size(); i++) {
            if (!files.get(i).isEmpty()) {
                try {
                    String realName = this.copyFile(files.get(i).getInputStream(), dir, files.get(i).getOriginalFilename());
                    UploadFile u = new UploadFile();
                    u.setRealName(realName);
                    u.setName(files.get(i).getOriginalFilename());
                    uploadFiles.add(u);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        request.setAttribute("uploadFiles", uploadFiles);
        return "success";
    }

    @RequestMapping("/download/{realName}/{name}")
    public void downloadObject(HttpServletResponse response, HttpServletRequest request, @PathVariable String realName, @PathVariable String name) {
        OutputStream os = null;
        response.reset();
        realName = request.getSession().getServletContext().getRealPath("/") + "upload" + File.separator + realName;
        response.setHeader("Content-Disposition", "attachment; filename=" + name);
        response.setContentType("application/octet-stream; charset=utf-8");
        try {
            os = response.getOutputStream();
            os.write(FileUtils.readFileToByteArray(new File(realName)));
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String copyFile(InputStream in, String dir, String fileName) throws IOException {
        fileName = fileName.substring(fileName.lastIndexOf("."));
        String realName = UUID.randomUUID().toString() + fileName;
        File file = new File(dir, realName);
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }
        FileUtils.copyInputStreamToFile(in, file);
        return realName;
    }
}
