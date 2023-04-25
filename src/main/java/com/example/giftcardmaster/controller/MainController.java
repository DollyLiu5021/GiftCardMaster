package com.example.giftcardmaster.controller;

import com.example.giftcardmaster.data.dbentity.GiftCardEntity;
import com.example.giftcardmaster.data.dbentity.UserEntity;
import com.example.giftcardmaster.service.GiftCardProductService;
import com.example.giftcardmaster.service.GiftCardService;
import com.example.giftcardmaster.service.OrderService;
import com.example.giftcardmaster.service.UserService;
import com.example.giftcardmaster.util.ConstUtil;
import com.example.giftcardmaster.util.EncryptionUtil;
import com.example.giftcardmaster.util.IpUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class MainController extends BaseController {

    // logger
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    UserService userService;

    @Autowired
    GiftCardService giftCardService;

    @Autowired
    GiftCardProductService giftCardProductService;

    @Autowired
    OrderService orderService;

    // --- Get Pages ---

    @RequestMapping(value = "/home")
    public String getHomePage(HttpServletRequest request,
                              Model model) {
        String countryCode = userService.getCountryCodeBasedOnIp(IpUtil.getIpAddr(request));
        model.addAttribute("SelectValue", 0);
        model.addAttribute("CategoryMap", giftCardService.getGiftCardsGroupByCategory());
        model.addAttribute("GiftCardProductList", giftCardProductService.getAllProducts(countryCode));
        return "home";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String geLoginPage() {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.isAuthenticated()) {
            return "redirect:home";
        }
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String userLogin(@RequestParam String username,
                            @RequestParam String password,
                            @RequestParam(defaultValue = "false") boolean rememberMe,
                            Model model) {
        String errorMsg;

        // Check if input params are illegal
        if (username.isEmpty() || password.isEmpty()
                || username.contains("#") || password.contains("#")) {
            errorMsg = "You are trying to bypass our security check. " +
                    "If you try for the second time your ip will be blocked.";
            model.addAttribute("errorMsg", errorMsg);
            return "login";
        }

        // Encrypt password
        password = EncryptionUtil.getUserEncryptedPassword(password);

        // Get current user
        Subject currentUser = SecurityUtils.getSubject();

        // Create token based on user input
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password, rememberMe);

        try {
            // Try to login via token
            currentUser.login(usernamePasswordToken);

        } catch (UnknownAccountException uae) {

            errorMsg = "Incorrect Username or Password";
            model.addAttribute("errorMsg", errorMsg);
            return "login";

        } catch (LockedAccountException lae) {

            errorMsg = "Account is Locked, Please contact Admin";
            model.addAttribute("errorMsg", errorMsg);
            return "login";

        } catch (AuthenticationException ae) {

            errorMsg = "Incorrect Username or Password";
            model.addAttribute("errorMsg", errorMsg);
            return "login";
        }

        return "redirect:/home";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String userRegister(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String email,
                               Model model) {
        String message;

        // Check if input params are illegal
        if (username.isEmpty() || password.isEmpty() || email.isEmpty()
                || username.contains("#") || password.contains("#") || email.contains("#")) {
            message = "You are trying to bypass our security check. " +
                    "If you try for the second time your ip will be blocked.";
            model.addAttribute("errorMsg", message);
            return "login";
        }

        // Encrypt password
        password = EncryptionUtil.getUserEncryptedPassword(password);

        // Check if information already exist
        message = userService.validateUserRegisterData(username, password, email);
        if (!message.equals(ConstUtil.SUCCESS)) {
            // Return Error Message
            model.addAttribute("errorMsg", message);
            return "login";
        }

        // Create user
        try {
            userService.createUserEntity(username, password, email);
        } catch (Exception e) {
            // Create User Failed
            message = "DataBase Error, Please contact Admin";
            model.addAttribute("errorMsg", message);
            return "login";
        }

        // Auto login
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
        currentUser.login(usernamePasswordToken);
        return "redirect:/home";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String userLogout() {
        SecurityUtils.getSubject().logout();
        return "redirect:/login";
    }

    @RequestMapping(value = "/404")
    public String get404Page() {
        return "/error/404";
    }

    @RequestMapping(value = "/home/search", method = RequestMethod.GET)
    public String searchGiftCards(@RequestParam(required = false) Long giftCardId,
                                  @RequestParam(required = false) String searchStr,
                                  @RequestParam(required = false) Integer selectIndex,
                                  HttpServletRequest request,
                                  Model model) {
        String countryCode = userService.getCountryCodeBasedOnIp(IpUtil.getIpAddr(request));

        // If we get giftCardId, then search for it
        if (giftCardId != null) {
            model.addAttribute("SelectValue", 0);
            model.addAttribute("GiftCardProductList", giftCardProductService.getByGiftCardId(giftCardId, countryCode));
        }
        // If we get searchStr, search for it
        else if (searchStr != null && !searchStr.isEmpty()) {
            // 1. First search in giftCard list
            List<GiftCardEntity> giftCardList = giftCardService.fuzzySearchByName(searchStr);
            // 2. If giftCard list is empty, we search for category
            if (giftCardList.isEmpty()) {
                giftCardList = giftCardService.fuzzySearchByCategoryName(searchStr);
            }
            List<Long> ids = new ArrayList<>();
            for (GiftCardEntity giftCard : giftCardList)
                ids.add(giftCard.getId());
            model.addAttribute("SelectValue", 0);
            model.addAttribute("GiftCardProductList", giftCardProductService.getByGiftCardIds(ids, countryCode));
        }
        // OrderBy user selection
        else if (selectIndex != null) {
            switch (selectIndex) {
                case 1:
                    model.addAttribute("SelectValue", 1);
                    model.addAttribute("GiftCardProductList", giftCardProductService.getAllProductsByDiscount(countryCode));
                    break;
                case 2:
                    model.addAttribute("SelectValue", 2);
                    model.addAttribute("GiftCardProductList", giftCardProductService.getAllProductsByUpdateTime(countryCode));
                    break;
                default:
                    model.addAttribute("SelectValue", 0);
                    model.addAttribute("GiftCardProductList", giftCardProductService.getAllProducts(countryCode));
            }
        }
        // If we get nothing, we return all cards
        else {
            model.addAttribute("SelectValue", 0);
            model.addAttribute("GiftCardProductList", giftCardProductService.getAllProducts(countryCode));
        }
        return "home::feature_fragment";
    }

    @RequestMapping(value = "/your-giftcards", method = RequestMethod.GET)
    public String getUserGiftCardsPage(Model model) {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.isAuthenticated()) {
            String userName = currentUser.getPrincipal().toString();
            UserEntity user = userService.getByUsername(userName);
            model.addAttribute("UserOrders", orderService.getByUserId(user.getId()));
            return "your-giftcards";
        }
        return "login";
    }

    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public String getAccountPage(Model model) {
        Subject currentUser = SecurityUtils.getSubject();
        UserEntity user = userService.getByUsername((String) currentUser.getPrincipal());
        model.addAttribute("UserInfo", user);
        return "account";
    }

}
