package com.cos.web.controller;

import com.cos.domain.Subscriber;
import com.cos.service.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by TQ3A016 on 2/26/2015.
 */
@Controller
@RequestMapping
public class SubscriberController {
    @Autowired
    private SubscriberService subscriberService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String toLogin() {
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(HttpSession httpSession, Model model, Subscriber subscriber) {
        if (subscriberService.authenticate(subscriber)) {
            httpSession.setAttribute("user", subscriber);
            return "showUser";
        }
        model.addAttribute("err", "bad user");
        return "login";
    }

    @RequestMapping(value="/register", method = RequestMethod.GET)
    public String toRegister() {
        return "register";
    }

    @RequestMapping(value="/register", method = RequestMethod.POST)
    public String register(HttpSession httpSession, Subscriber subscriber) {
        subscriberService.register(subscriber);
        httpSession.setAttribute("user", subscriber);
        return "showUser";
    }

    @RequestMapping(value="/showUser")
    public String showUser() {
        return "showUser";
    }
}
