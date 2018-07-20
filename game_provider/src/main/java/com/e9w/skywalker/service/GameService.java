package com.e9w.skywalker.service;

import com.jfinal.plugin.activerecord.Db;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by fc on 2016-12-06.
 */
@RestController
@RequestMapping(value = "/internal/game")
public class GameService extends BaseService {

    private Logger logger = LoggerFactory.getLogger(GameService.class);

    @RequestMapping(value = "/getAllActiveGames", method = RequestMethod.POST)
    public List<Map<String, Object>> getAllActiveGames(int channelId, int platformId) {
        String where = " WHERE c.channel_id = " + channelId + " AND c.platform_id = " + platformId;
        return getActiveGamesWithWhere(where);
    }

    @RequestMapping(value = "/getActiveGamesByTypeId", method = RequestMethod.GET)
    public List<Map<String, Object>> getActiveGamesByTypeId(int channelId, int platformId, int typeId) {
        String where = " WHERE c.channel_id = " +channelId + " AND c.platform_id = " +platformId + " AND c.category_id = " + typeId;
        return getActiveGamesWithWhere(where);
    }

    private List<Map<String, Object>> getActiveGamesWithWhere(String where) {
        try {
            String sql = "SELECT p.product_name, p.product_description, IFNULL(c.package_name, '') package_name , " +
                    " pc.category_name, v.version_code , IF(a.file_id IS NULL, '', CONCAT(ap.host_url, a.filepath)) file_path, " +
                    " IF(f.file_id IS NULL, '', CONCAT(fp.host_url, f.filepath)) icon1_path, " +
                    " IF(f2.file_id IS NULL, '', CONCAT(f2p.host_url, f2.filepath)) icon2_path, " +
                    " IF(f3.file_id IS NULL, '', CONCAT(f3p.host_url, f3.filepath)) icon3_path, " +
                    " IF(i.file_id IS NULL, '', CONCAT(ip.host_url, i.filepath)) image1_path, " +
                    " IF(i2.file_id IS NULL, '', CONCAT(i2p.host_url, i2.filepath)) image2_path, " +
                    " IF(i3.file_id IS NULL, '', CONCAT(i3p.host_url, i3.filepath)) image3_path, " +
                    " IF(i4.file_id IS NULL, '', CONCAT(i4p.host_url, i4.filepath)) image4_path, " +
                    " IF(i5.file_id IS NULL, '', CONCAT(i5p.host_url, i5.filepath)) image5_path " +
                    " FROM dis_product p " +
                    " INNER JOIN dis_product_channel c ON p.product_id = c.product_id AND c.enabled = 1 AND c.deleted = 0 " +
                    " INNER JOIN dis_product_channel_status s ON s.status_id = c.status_id AND s.is_visible = 1 " +
                    " INNER JOIN dis_product_channel_platform cp ON cp.platform_id = c.platform_id AND cp.enabled = 1 and cp.deleted = 0 " +
                    " INNER JOIN dis_product_version v ON v.version_id = c.version_id AND v.enabled = 1 AND v.deleted = 0 AND v.actived = 1 " +
                    " INNER JOIN dis_product_category pc ON pc.category_id = p.category_id " +
                    " LEFT JOIN skywalker.razor_filestorage a ON a.file_id = v.file_id " +
                    " LEFT JOIN skywalker.razor_filestorage f ON f.file_id = v.icon1_id " +
                    " LEFT JOIN skywalker.razor_filestorage f2 ON f2.file_id = v.icon2_id " +
                    " LEFT JOIN skywalker.razor_filestorage f3 ON f3.file_id = v.icon3_id " +
                    " LEFT JOIN skywalker.razor_filestorage i ON i.file_id = v.image1_id " +
                    " LEFT JOIN skywalker.razor_filestorage i2 ON i2.file_id = v.image2_id " +
                    " LEFT JOIN skywalker.razor_filestorage i3 ON i3.file_id = v.image3_id " +
                    " LEFT JOIN skywalker.razor_filestorage i4 ON i4.file_id = v.image4_id " +
                    " LEFT JOIN skywalker.razor_filestorage i5 ON i5.file_id = v.image5_id " +
                    " LEFT JOIN skywalker.razor_filestorage_provider ap ON ap.provider_id = a.provider_id " +
                    " LEFT JOIN skywalker.razor_filestorage_provider fp ON fp.provider_id = f.provider_id " +
                    " LEFT JOIN skywalker.razor_filestorage_provider f2p ON f2p.provider_id = f2.provider_id " +
                    " LEFT JOIN skywalker.razor_filestorage_provider f3p ON f3p.provider_id = f3.provider_id " +
                    " LEFT JOIN skywalker.razor_filestorage_provider ip ON ip.provider_id = i.provider_id " +
                    " LEFT JOIN skywalker.razor_filestorage_provider i2p ON i2p.provider_id = i2.provider_id " +
                    " LEFT JOIN skywalker.razor_filestorage_provider i3p ON i3p.provider_id = i3.provider_id " +
                    " LEFT JOIN skywalker.razor_filestorage_provider i4p ON i4p.provider_id = i4.provider_id " +
                    " LEFT JOIN skywalker.razor_filestorage_provider i5p ON i5p.provider_id = i5.provider_id " +
//                " WHERE c.channel_id = ? AND c.type_id = ? AND c.platform_id = ? ORDER BY p.priority"
                    where +
                    " ORDER BY p.priority";
            List<Map<String, Object>> games = record2list(Db.find(sql));
            if (null == games) {
                games = new ArrayList<>();
            }
            return games;
        } catch (Throwable t) {
            logger.error("getActiveGamesWithWhere exception = {}", t);
            throw t;
        }
    }
}
