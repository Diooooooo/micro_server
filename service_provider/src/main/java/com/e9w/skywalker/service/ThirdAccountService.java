package com.e9w.skywalker.service;

import com.e9w.skywalker.util.Md5Util;
import com.jfinal.plugin.activerecord.Db;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by fc on 2016-12-06.
 */
@RestController
@RequestMapping(value = "/internal/users")
public class ThirdAccountService extends BaseService {

    @RequestMapping(value = "/loginWithDeviceInfo", method = RequestMethod.POST)
    public Map<String, Object> loginWithDeviceInfo(String imei, String cellphone, String password, String nickname) {
        createUser(imei, cellphone, password, nickname);
        String sql = "SELECT u.imei imei, u.cellphone, u.nickname, u.sessionkey FROM dis_users u " +
                "WHERE u.imei = ? AND u.cellphone = ? AND u.sessionkey = ?";
        Map<String, Object> info = record2map(Db.findFirst(sql, imei, cellphone, Md5Util.digest(imei)));
        return info;
    }

    @RequestMapping(value = "/loginBySessionkey", method = RequestMethod.GET)
    public Map<String, Object> loginBySessionkey(String sessionkey) {
        String sql = "SELECT u.imei, u.cellphone, u.nickname, u.sessionkey FROM dis_users u WHERE u.sessionkey = ?";
        return record2map(Db.findFirst(sql, sessionkey));
    }

    private void createUser(String imei, String cellphone, String password, String nickname) {
        String sql = "INSERT dis_users (cellphone, imei, password, nickname, last_login, sessionkey) VALUES (?, ?, ?, ?, now(), md5(?))" +
                " ON DUPLICATE KEY UPDATE imei = VALUES(imei), cellphone = VALUES(cellphone), last_login = VALUES(last_login)";
        Db.update(sql, cellphone, imei, password, nickname, imei);
    }

}
