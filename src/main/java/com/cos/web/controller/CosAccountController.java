package com.cos.web.controller;

/**
 * Created by Joseph on 2015/2/28.
 */

import com.cos.domain.CosAccount;
import com.cos.domain.Subscriber;
import com.cos.service.CosAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping(value = "/cos/admin")
public class CosAccountController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CosAccountController.class);
    @Autowired
    private CosAccountService cosAccountService;

    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    public String listAccount(HttpSession httpSession, Model model) {
        Subscriber subscriber = (Subscriber)httpSession.getAttribute("subscriber");
        if (subscriber == null) {
            model.addAttribute("err", "user should log in first");
            return "redirect:/login";
        }

        List<CosAccount> list = cosAccountService.list(subscriber.getName());
        model.addAttribute("cosAccounts", list);
        return "listCos";
    }

    @RequestMapping(value = "/addCos", method = RequestMethod.GET)
    public String toAdd() {
        return "addCos";
    }

    @RequestMapping(value = "/addCos", method = RequestMethod.POST)
    public String add(CosAccount cosAccount) {
        cosAccountService.add(cosAccount);
        return "redirect:accounts";
    }

}
