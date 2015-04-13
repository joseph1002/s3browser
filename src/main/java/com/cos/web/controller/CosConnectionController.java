package com.cos.web.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.cos.domain.Subscriber;
import com.cos.service.CosConnectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Joseph on 3/4/2015.
 */
@Controller
@Scope("prototype")
@RequestMapping(value = "/cos")
public class CosConnectionController {
    private static final Logger logger = LoggerFactory.getLogger(CosConnectionController.class);
    @Autowired
    CosConnectionService cosConnectionService;

    @RequestMapping(value = "/entry", method = RequestMethod.POST)
    @ResponseBody
    public Object connect(HttpSession httpSession, String host, String accessKey, String secretKey, String protocol) {
        AmazonS3 conn = cosConnectionService.connect(host, accessKey, secretKey, protocol);
        httpSession.setAttribute("connection", conn);

        Map<String, Object> modelMap = new HashMap<String, Object>();
        modelMap.put("accountOwner", conn.getS3AccountOwner());
        return modelMap;
    }

    @RequestMapping(value = "/buckets", method = RequestMethod.GET)
    @ResponseBody
    public Object listBuckets(HttpSession httpSession, Model model) {
        AmazonS3 conn = (AmazonS3)httpSession.getAttribute("connection");
//        List<Bucket> bucketList = cosConnectionService.listBuckets(conn);
//        model.addAttribute("buckets", bucketList);
//        return "listBuckets";
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = objectMapper.writeValueAsString(bucketList);
        return cosConnectionService.listBuckets(conn);
    }

    @RequestMapping(value = "/bucket/{bucketName}", method = RequestMethod.POST)
    @ResponseBody
    public Object createBucket(HttpSession httpSession, @PathVariable(value="bucketName") String bucketName) {
        AmazonS3 conn = (AmazonS3)httpSession.getAttribute("connection");
        return cosConnectionService.createBucket(conn, bucketName);
    }

    @RequestMapping(value = "/bucket/{bucketName}", method = RequestMethod.DELETE)
    @ResponseBody
    public Object deleteBucket(HttpSession httpSession, @PathVariable(value="bucketName") String bucketName) {
        AmazonS3 conn = (AmazonS3)httpSession.getAttribute("connection");
        cosConnectionService.deleteBucket(conn, bucketName);

        Map<String, Object> modelMap = new HashMap<String, Object>();
        modelMap.put("deletedBucket", bucketName);
        return modelMap;
    }

    @RequestMapping(value = "/bucket/{bucketName}/objects", method = RequestMethod.GET)
    @ResponseBody
    public Object listObjects(HttpSession httpSession, Model model, @PathVariable(value="bucketName") String bucketName) {
        AmazonS3 conn = (AmazonS3)httpSession.getAttribute("connection");
//        List<S3ObjectSummary> objectSummaryList = cosConnectionService.listObjects(conn, bucketName);
//        model.addAttribute("objects", objectSummaryList);
//        return "listObjects";
        return cosConnectionService.listObjects(conn, bucketName);
    }

    @RequestMapping(value = "/bucket/{bucketName}/object", method = RequestMethod.POST)
    public String uploadObjects(HttpSession httpSession, MultipartHttpServletRequest request, @PathVariable(value="bucketName") String bucketName) {
        AmazonS3 conn = (AmazonS3)httpSession.getAttribute("connection");
        List<MultipartFile> files = request.getFiles("file");
//        Subscriber subscriber = (Subscriber)httpSession.getAttribute("subscriber");
//        String dir = request.getSession().getServletContext().getRealPath("/") + File.separator + "uploads" + File.separator + subscriber.getName();

        for (MultipartFile multiFile : files) {
            if (!multiFile.isEmpty()) {
                try {
                    String originalFilename = multiFile.getOriginalFilename();
                    cosConnectionService.putObject(conn, bucketName, originalFilename, multiFile.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "redirect:objects";
    }

    @RequestMapping("/bucket/{bucketName}/object/{objectName}")
    public void downloadObject(HttpSession httpSession, HttpServletResponse response, HttpServletRequest request, @PathVariable String bucketName, @PathVariable String objectName) throws UnsupportedEncodingException {
        AmazonS3 conn = (AmazonS3)httpSession.getAttribute("connection");
        String lastDir = "anonymous";
        Subscriber subscriber = (Subscriber)httpSession.getAttribute("subscriber");
        if (subscriber != null) {
            lastDir = subscriber.getName();
        }
        String dir = request.getSession().getServletContext().getRealPath("/") + File.separator
                + "downloads" + File.separator + lastDir;

        OutputStream os = null;
        response.reset();

        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(objectName, "UTF-8"));

        try {
            os = response.getOutputStream();
            cosConnectionService.getObject(conn, bucketName, objectName, dir, os);
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

    @RequestMapping(value = "/bucket/{bucketName}/object/{objectName}/acl/{accessControl}", method = RequestMethod.PUT)
    public void changeObjectAcl(HttpSession httpSession, @PathVariable String bucketName, @PathVariable String objectName, @PathVariable String accessControl) {
        AmazonS3 conn = (AmazonS3)httpSession.getAttribute("connection");
        cosConnectionService.changeObjectAcl(conn, bucketName, objectName, accessControl);
    }
}
