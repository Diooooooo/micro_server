package com.e9w.skywalker.service;

import com.netflix.appinfo.InstanceInfo;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * Created by fc on 2016-12-12.
 */
public interface IGameService {

    List<Map<String, Object>> getAllActiveGames(@RequestParam(name = "info")InstanceInfo info,
                                                @RequestParam(name = "method")String method,
                                                @RequestParam(name = "clazz")Class<?> clazz,
                                                @RequestParam(name = "platformId") int platformId,
                                                @RequestParam(name = "channelId") int channelId);

    List<Map<String, Object>> getActiveGamesByTypeId(@RequestParam(name = "info")InstanceInfo info,
                                                     @RequestParam(name = "method")String method,
                                                     @RequestParam(name = "clazz")Class<?> clazz,
                                                     @RequestParam(name = "platformId")int platformId,
                                                     @RequestParam(name = "channelId")int channelId,
                                                     @RequestParam(name = "typeId")int typeId);
}
