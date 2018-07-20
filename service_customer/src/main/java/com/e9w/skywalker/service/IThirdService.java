package com.e9w.skywalker.service;

import com.netflix.appinfo.InstanceInfo;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Created by fc on 2016-12-21.
 */
public interface IThirdService {

    Map<String, Object> loginWithDeviceInfo(@RequestParam(name = "info")InstanceInfo instanceinfo,
                                            @RequestParam(name = "method")String method,
                                            @RequestParam(name = "clazz")Class<?> clazz,
                                            @RequestParam(name = "imei")String imei,
                                            @RequestParam(name = "cellphone")String cellphone,
                                            @RequestParam(name = "password")String password,
                                            @RequestParam(name = "nickname")String nickname);

    Map<String, Object> loginBySessionkey(@RequestParam(name = "info")InstanceInfo info,
                                          @RequestParam(name = "method")String method,
                                          @RequestParam(name = "clazz")Class<?> clazz,
                                          @RequestParam(name = "sessionkey")String sessionkey);
}
