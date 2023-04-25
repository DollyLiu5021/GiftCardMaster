package com.example.giftcardmaster.controller;

import com.example.giftcardmaster.data.dbentity.UserEntity;
import com.example.giftcardmaster.service.UserService;
import com.example.giftcardmaster.util.ConstUtil;
import com.example.giftcardmaster.util.EncryptionUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    // logger
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @RequestMapping(value = "/info/update", method = RequestMethod.POST)
    public String updateUserInfo(@RequestParam String password,
                                 @RequestParam String email,
                                 @RequestParam String phone,
                                 Model model) {

        // Check if input params are illegal
        if (password.isEmpty() || email.isEmpty()
                || password.contains("#") || email.contains("#")) {
            String errorMsg = "You are trying to bypass our security check. " +
                    "If you try for the second time your ip will be blocked.";
            model.addAttribute("errorMsg", errorMsg);
            return "account::main_fragment";
        }

        // Encrypt password
        password = EncryptionUtil.getUserEncryptedPassword(password);

        Subject currentUser = SecurityUtils.getSubject();
        UserEntity user = userService.getByUsername((String) currentUser.getPrincipal());
        if (!password.equals(user.getPassword())) {
            model.addAttribute("Message", "Wrong Password, Please Check");
            model.addAttribute("UserInfo", user);
            return "account::main_fragment";
        }
        if(!email.equals(user.getEmail()) && !userService.validateUserEmail(email).equals(ConstUtil.SUCCESS)) {
            model.addAttribute("Message", "Email Already Exist");
            model.addAttribute("UserInfo", user);
            return "account::main_fragment";
        }

        user.setEmail(email);
        user.setPhone(phone);
        userService.updateUserInfo(user);

        model.addAttribute("Message", "Update Success");
        model.addAttribute("UserInfo", user);
        return "account::main_fragment";
    }

    @RequestMapping(value = "/password/update", method = RequestMethod.POST)
    public String updateUserPassword(@RequestParam String oldPassword,
                                     @RequestParam String newPassword,
                                     Model model) {

        // Check if input params are illegal
        if (oldPassword.isEmpty() || newPassword.isEmpty()
                || oldPassword.contains("#") || newPassword.contains("#")) {
            String errorMsg = "You are trying to bypass our security check. " +
                    "If you try for the second time your ip will be blocked.";
            model.addAttribute("errorMsg", errorMsg);
            return "account::main_fragment";
        }

        // Encrypt password
        oldPassword = EncryptionUtil.getUserEncryptedPassword(oldPassword);

        Subject currentUser = SecurityUtils.getSubject();
        UserEntity user = userService.getByUsername((String) currentUser.getPrincipal());
        if (!oldPassword.equals(user.getPassword())) {
            model.addAttribute("Message", "Wrong Password, Please Check");
            model.addAttribute("UserInfo", user);
            return "account::main_fragment";
        }

        newPassword = EncryptionUtil.getUserEncryptedPassword(newPassword);
        user.setPassword(newPassword);
        userService.updateUserInfo(user);

        model.addAttribute("Message", "Update Success");
        model.addAttribute("UserInfo", user);
        return "account::main_fragment";
    }
}
