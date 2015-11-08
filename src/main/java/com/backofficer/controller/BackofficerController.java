package com.backofficer.controller;

import com.backofficer.Query.QueryTask;
import com.backofficer.message.Message;
import com.backofficer.message.MessageDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.annotation.PostConstruct;
import java.util.*;

@Controller
public class BackofficerController{

    private ApplicationContext context;

    @Autowired
    private MessageDAO message;
    @Autowired
    private QueryTask qt;

    public BackofficerController(){

    }

    @PostConstruct
    public void NewQueryTask(){
        qt.run();
    }

    @RequestMapping("/main")
    public ModelAndView main() {
        return new ModelAndView("main", "message", "");
    }

    @RequestMapping(value = "/getlist")
    public @ResponseBody
    List<Message> getList() {
        return message.getAllMessages();
    }

    @RequestMapping(value="/updatelist")
    @ResponseBody
    public DeferredResult<List<Message>> ajaxReply() throws Exception {
        final DeferredResult<List<Message>> dr = new DeferredResult<List<Message>>(
                30000);
        qt.addSubscribed(dr);

        dr.onTimeout(new Runnable() {
            @Override
            public void run() {
                dr.setResult(null);
            }
        });

        return dr;
    }

}