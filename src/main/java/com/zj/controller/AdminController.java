package com.zj.controller;

import com.zj.constants.RedisPrefix;
import com.zj.utils.ValidateCodeUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@CrossOrigin
public class AdminController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /*
    * 为前端返回字符验证码
    * @return
    */
    @RequestMapping("/captcha")
    public Map<String , String> captcha(){
        //返回验证码
        HashMap<String , String > hashMap = new HashMap<>();
        //为每一个验证码生成一个唯一  key
        String  token = DigestUtils.md5DigestAsHex(UUID.randomUUID().toString().getBytes());
        System.out.println(token);
        //生成value 字符串的值
        ValidateCodeUtil.Validate validate = ValidateCodeUtil.getRandomCode();
        String value = validate.getValue();
        System.out.println("value = "+ value);
        // 将验证码保存到redis中
        stringRedisTemplate.opsForValue().set(RedisPrefix.CAPTCHA_CODE + token, value, 10, TimeUnit.MINUTES);

        //生成base64
        String base64Str = validate.getBase64Str();
        //返回base64
        hashMap.put("code", "data:image/png;base64," + base64Str);
        return  hashMap;
    }
}
