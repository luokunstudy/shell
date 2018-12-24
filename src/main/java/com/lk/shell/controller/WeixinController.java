package com.lk.shell.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/weixin")
@Slf4j
public class WeixinController {
    @GetMapping(value = "/auto")
    public void auto(@RequestParam("code") String code){
        log.info("进入auto方法......");
        log.info("code={}",code);
        String url ="https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx75eab88f02a2e739&secret=4c64c995ac89aae39629c5f8e9244dfd&code=\" + code + \"&grant_type=authorization_code";
        RestTemplate restTemplate =new RestTemplate();
        String respose =restTemplate.getForObject(url,String.class);
        log.info("respose={}",respose);
    }
}
