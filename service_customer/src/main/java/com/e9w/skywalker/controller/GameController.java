package com.e9w.skywalker.controller;

import com.e9w.skywalker.service.IGameService;
import com.e9w.skywalker.service.factory.ServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by fc on 2016-12-13.
 */
@RestController
public class GameController extends ApiController {

    private final static Logger logger = LoggerFactory.getLogger(GameController.class);
    private final static String  SERVICE_PROVIDER = "GAME_PROVIDER";

    @PostMapping("/getAllActiveGames")
    public void getAllActiveGames(int channelId, int platform) {
        try {
            List<Map<String, Object>> games = ServiceFactory.getService(IGameService.class).getAllActiveGames(
                    getInstanceInfo(SERVICE_PROVIDER, false),
                   getServiceInstance(SERVICE_PROVIDER).getUri() + "/internal/game/getAllActiveGames",
                    List.class, platform, channelId);
            this.renderData(games);
        } catch (Throwable t) {
            this.renderException(t.getMessage());
        }
    }

    @PostMapping("/getActiveGamesByTypeId")
    public void getActiveGamesByTypeId(int channelId, int platform, int typeId) {
        try {
            List<Map<String, Object>> games = ServiceFactory.getService(IGameService.class).getActiveGamesByTypeId(
                    getInstanceInfo(SERVICE_PROVIDER, false), getServiceInstance(SERVICE_PROVIDER).getUri() + "/internal/game/getActiveGamesByTypeId?",
                    List.class, platform, channelId, typeId);
            this.renderData(games);
        } catch (Throwable t) {
            this.renderException(t.getMessage());
        }
    }
}
