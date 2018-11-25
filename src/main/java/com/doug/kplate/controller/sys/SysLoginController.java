package com.doug.kplate.controller.sys;


import com.doug.kplate.entity.SysUserEntity;
import com.doug.kplate.service.SysUserService;
import com.doug.kplate.service.SysUserTokenService;
import com.doug.kplate.utils.R;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

/**
 * 登录相关
 *
 */
@RestController
public class SysLoginController {
    /*@Autowired(required = true)
    private Producer producer;*/
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserTokenService sysUserTokenService;

    /*@RequestMapping("captcha.jpg")
    public void captcha(HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("images/jpeg");

        //生成文字验证码
        String text = producer.createText();
        //生成图片验证码
        BufferedImage image = producer.createImage(text);
        //保存到shiro session
        ShiroUtils.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, text);

        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        IOUtils.closeQuietly(out);
    }*/

    /**
     * 登录
     */
    @RequestMapping(value = "/sys/login", method = RequestMethod.POST)
//    String captcha,
    public Map<String, Object> login(String username, String password) throws IOException {

//        String appId = "57d01e05e810b0074b2405d25e1c7ea6";
//        String appSecret = "afb438a28e7a95074c03684456de4ef4";
//        CaptchaClient captchaClient = new CaptchaClient(appId, appSecret);
//        //captchaClient.setCaptchaUrl(captchaUrl);
//        //特殊情况需要额外指定服务器,可以在这个指定，默认情况下不需要设置
//        CaptchaResponse response = null;
//        try {
//            response = captchaClient.verifyToken(token);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println(response.getCaptchaStatus());
//        //确保验证状态是SERVER_SUCCESS，SDK中有容错机制，在网络出现异常的情况会返回通过
////        if (response.getResult()) {
//        if (response.getCaptchaStatus().equals("SERVER_SUCCESS")) {
////            /**token验证通过，继续其他流程**/
////            String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
////            if (!captcha.equalsIgnoreCase(kaptcha)) {
////                return R.error("验证码不正确");
////            }

            //用户信息
            SysUserEntity user = sysUserService.queryByUserName(username);

            //账号不存在、密码错误
            if (user == null || !user.getPassword().equals(new Sha256Hash(password, user.getSalt()).toHex())) {
                return R.error("账号或密码不正确");
            }

            //账号锁定
            if (user.getStatus() == 0) {
                return R.error("账号已被锁定,请联系管理员");
            }

            //生成token，并保存到数据库
            R r = sysUserTokenService.createToken(user.getUserId());
            return r;
//        } else {
//            /**token验证失败，业务系统可以直接阻断该次请求或者继续弹验证码**/
//            return R.error("验证失败!");
//        }
    }

    public static void main(String[] args) {
        System.out.println(new Sha256Hash("123456", "YzcmCZNvbXocrsz9dm8e").toHex());
    }
}
