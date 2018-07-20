package com.e9w.skywalker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by fc on 2016-11-25.
 */
@RestController
public class HomeController extends ApiController {

    private final static String THIRDACCOUNT_PROVIDER = "THIRDACCOUNT_PROVIDER";

    @GetMapping("/serviceInstance")
    public void uri() {
        this.renderData(getServiceInstance(THIRDACCOUNT_PROVIDER).toString());
    }

    @GetMapping("/instanceInfo")
    public void providerInfo() {
        this.renderData(getInstanceInfo(THIRDACCOUNT_PROVIDER, false));
    }
}
