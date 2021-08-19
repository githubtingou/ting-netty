package com.ting.netty.netty.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Random;

/**
 * 首页
 *
 * @author lishuang
 * @version 1.0
 * @date 2021/8/16
 */
@Controller
public class IndexController {

    /**
     *
     */
    @GetMapping(value = "index")
    public ModelAndView index() {
        ModelAndView view = new ModelAndView("socket");

        view.addObject("uid", new Random().nextInt(6));
        return view;
    }
}
