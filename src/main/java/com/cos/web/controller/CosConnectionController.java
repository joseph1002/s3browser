package com.cos.web.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.cos.service.CosConnectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

/**
 * Created by TQ3A016 on 3/4/2015.
 */
@Controller
public class CosConnectionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CosConnectionController.class);
    @Autowired
    CosConnectionService cosConnectionService;

    @RequestMapping(value = "/connectCos", method = RequestMethod.POST)
    public String connectCos(HttpSession httpSession, String host, String accessKey, String secretKey) {
        AmazonS3 conn = cosConnectionService.connect(host, accessKey, secretKey);
        httpSession.setAttribute("connection", conn);
        return "";
    }



}
