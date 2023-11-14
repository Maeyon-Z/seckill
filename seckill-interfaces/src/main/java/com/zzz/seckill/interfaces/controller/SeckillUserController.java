package com.zzz.seckill.interfaces.controller;

import com.zzz.seckill.application.service.SeckillUserService;
import com.zzz.seckill.domain.code.HttpCode;
import com.zzz.seckill.domain.model.dto.SeckillUserDTO;
import com.zzz.seckill.domain.response.ResponseMessage;
import com.zzz.seckill.domain.response.ResponseMessageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zzz.seckill.domain.model.entity.SeckillUser;
@RestController
@RequestMapping(value = "/user")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", originPatterns = "*")
public class SeckillUserController {

    @Autowired
    private SeckillUserService seckillUserService;
    /**
     * 测试系统
     */
    @RequestMapping(value = "/get", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseMessage<SeckillUser> getUser(@RequestAttribute Long userId){
        return ResponseMessageBuilder.build(HttpCode.SUCCESS.getCode(), seckillUserService.getSeckillUserByUserId(userId));
    }
    /**
     * 登录系统
     */
    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseMessage<String> login(@RequestBody SeckillUserDTO seckillUserDTO){
        return ResponseMessageBuilder.build(HttpCode.SUCCESS.getCode(), seckillUserService.login(seckillUserDTO.getUserName(), seckillUserDTO.getPassword()));
    }
}

