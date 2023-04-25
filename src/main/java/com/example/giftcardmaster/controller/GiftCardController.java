package com.example.giftcardmaster.controller;

import com.example.giftcardmaster.data.dbentity.GiftCardProductEntity;
import com.example.giftcardmaster.data.dbentity.OrderDetailGiftCardEntity;
import com.example.giftcardmaster.data.dbentity.OrderEntity;
import com.example.giftcardmaster.data.dbentity.UserEntity;
import com.example.giftcardmaster.data.httpentity.BaseResponseEnitty;
import com.example.giftcardmaster.data.httpentity.CitconPaymentEntity;
import com.example.giftcardmaster.service.*;
import com.example.giftcardmaster.util.ConstUtil;
import com.example.giftcardmaster.util.EncryptionUtil;
import com.example.giftcardmaster.util.IpUtil;
import com.example.giftcardmaster.util.PaymentUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;

@Controller
@RequestMapping("/giftcard")
public class GiftCardController {

    // logger
    private static final Logger logger = LoggerFactory.getLogger(GiftCardController.class);

    @Autowired
    GiftCardService giftCardService;

    @Autowired
    GiftCardProductService giftCardProductService;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderDetailGiftCardService orderDetailGiftCardService;

    @Autowired
    PurchaseService purchaseService;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/product/{productId}", method = RequestMethod.GET)
    public String getGiftCardProductDetailPage(@PathVariable Long productId,
                                               HttpServletRequest request,
                                               Model model) {
        Subject currentUser = SecurityUtils.getSubject();
        UserEntity user = userService.getByUsername((String) currentUser.getPrincipal());

        String countryCode = userService.getCountryCodeBasedOnIp(IpUtil.getIpAddr(request));
        GiftCardProductEntity productEntity = giftCardProductService.getProductById(productId);

        int userDiscount = giftCardProductService.getUserDiscount(user, productEntity);

        BigDecimal usePurchasePrice = new BigDecimal(100 - userDiscount)
                .divide(new BigDecimal(100), 3, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(50)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal userSave = new BigDecimal(50).subtract(usePurchasePrice);

        model.addAttribute("User", user);
        model.addAttribute("UserDiscount", userDiscount);
        model.addAttribute("UserSave", userSave.toString());
        model.addAttribute("UserPrice", usePurchasePrice.toString());
        model.addAttribute("CountryCode", countryCode);
        model.addAttribute("GiftCardProduct", productEntity);
        return "giftcard-detail";
    }

    @RequestMapping(value = "/product/purchase", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public BaseResponseEnitty giftCardProductPurchase(@RequestParam Long productId,
                                          @RequestParam BigDecimal originPrice,
                                          @RequestParam String paymentMethod,
                                          HttpServletRequest request) {
        String countryCode = userService.getCountryCodeBasedOnIp(IpUtil.getIpAddr(request));
        Subject currentUser = SecurityUtils.getSubject();

        if (currentUser.isAuthenticated()) {
            // --- 1. Create order with pending status ---
            UserEntity user = userService.getByUsername((String) currentUser.getPrincipal());
            GiftCardProductEntity product = giftCardProductService.getProductById(productId);
            // If cannot find user or product information, return error page
            if (user == null || product == null) {
                new BaseResponseEnitty(500, "Cannot find user or product information", null);
            }

            // TODO: Check if current gift card is available 1) Yes then go on process 2) No return error


            int userDiscount = giftCardProductService.getUserDiscount(user, product);
            BigDecimal userPurchasePrice = new BigDecimal(100 - userDiscount)
                    .divide(new BigDecimal(100), 3, RoundingMode.HALF_UP)
                    .multiply(originPrice).setScale(2, RoundingMode.HALF_UP);


            // Create order entity
            OrderEntity order = new OrderEntity();
            order.setIdToken(EncryptionUtil.getOrderIdToken());
            order.setProductTypeId(1);
            order.setUserId(user.getId());
            order.setProductId(product.getId());
            order.setOriginPrice(originPrice);
            order.setDiscount(userDiscount);
            order.setPurchasePrice(userPurchasePrice);
            order.setPurchaseTime(new Timestamp(System.currentTimeMillis()));
            order.setOrderStatusId(4); // Pending status
            order.setPaymentMethod(paymentMethod);
            order.setUpdateBy(null);
            order.setCreateBy(user.getUsername());
            orderService.createOrderEntity(order);

            // Create order detail
            OrderDetailGiftCardEntity orderDetail = new OrderDetailGiftCardEntity();
            orderDetail.setOrderId(order.getId());
            orderDetail.setUpdateBy(null);
            orderDetail.setCreateBy(user.getUsername());
            orderDetailGiftCardService.createOrderDetail(orderDetail);

            // --- 2. Process purchase transaction ---
            CitconPaymentEntity paymentEntity = new CitconPaymentEntity();
            paymentEntity.setPaymentMethod(paymentMethod);
            paymentEntity.setAmount(userPurchasePrice.multiply(new BigDecimal(100)).intValue());
            paymentEntity.setCurrency(PaymentUtil.getCurrencyByContryCode(countryCode));
            paymentEntity.setReference(order.getIdToken());
            paymentEntity.setIpnUrl(ConstUtil.HOST + "/giftcard/product/citcon/ipn");
            paymentEntity.setCallbackUrlSuccess(ConstUtil.HOST + "/giftcard/product/citcon/success");
            paymentEntity.setCallbackUrlFail(ConstUtil.HOST + "/giftcard/product/citcon/fail");
            paymentEntity.setMobileResultUrl(ConstUtil.HOST + "/giftcard/product/citcon/mobileconfirm");
            paymentEntity.setAllowDuplicates("no");

            String result = purchaseService.citconPurchaseTransactionProcess(paymentEntity);
            if (result.equals("error")) {
                // Update order status to fail
                orderService.updateOrderEntityStatus(order.getId(), 2);
                return new BaseResponseEnitty(500,
                        "Currently our partner cannot process your transaction, please try another payment method or try again later", null);
            }
            else {
                return new BaseResponseEnitty(200, result, null);
            }
        }

        // User is not authenticated, return error page
        return new BaseResponseEnitty(500, "User is not authenticated", null);
    }

    // --- Call Back Interfaces ---

    @RequestMapping(value = "/product/citcon/ipn", method = RequestMethod.POST)
    public void getCitconIpnResponse() {
        purchaseService.citconGiftCardIpnHandler();
    }

    @RequestMapping(value = "/product/citcon/success", method = RequestMethod.GET)
    public String getCitconPaymentSuccessScreen() {
        return "citcon-payment-success";
    }

    @RequestMapping(value = "/product/citcon/fail", method = RequestMethod.GET)
    public String getCitconPaymentFailScreen() {
        return "citcon-payment-fail";
    }

    @RequestMapping(value = "/product/citcon/mobileconfirm", method = RequestMethod.GET)
    public String getCitconPaymentMobileConfirmScreen() {
        return "citcon-payment-mobile-confirm";
    }
}
