package com.e9w.skywalker.controller;

import com.e9w.skywalker.service.IThirdService;
import com.e9w.skywalker.service.factory.ServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by fc on 2016-12-23.
 */
@RestController
public class UserController extends ApiController {
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);
    private final static String USER_PROVIDER = "THIRDACCOUNT_PROVIDER";

    @PostMapping("/loginWithDeviceInfo")
    public void loginWithDeviceInfo(String cellphone, String imei, String nickname, String password) {
        try {
            Map<String, Object> userInfo = ServiceFactory.getService(IThirdService.class).loginWithDeviceInfo(
                    getInstanceInfo(USER_PROVIDER, false), getServiceInstance(USER_PROVIDER).getUri() + "/internal/users/loginWithDeviceInfo",
                    Map.class, imei, cellphone, password, nickname);
            this.renderData(userInfo);
        } catch (Throwable t) {
            this.renderException(t.getMessage());
        }
    }

    @GetMapping("/loginBySessionkey")
    public void loginBySessionkey(String sessionkey) {
        try {
            Map<String, Object> userInfo = ServiceFactory.getService(IThirdService.class).loginBySessionkey(
                    getInstanceInfo(USER_PROVIDER, false), getServiceInstance(USER_PROVIDER).getUri() + "/internal/users/loginBySessionkey?",
                    Map.class, sessionkey);
            this.renderData(userInfo);
        } catch (Throwable t) {
            this.renderException(t.getMessage());
        }
    }
}
