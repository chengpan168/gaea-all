/**
 *
 */
package com.gaea.service.helper;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gaea.common.http.HttpWebClient;
import com.gaea.dto.UserDto;
import com.google.common.collect.Maps;

/**
 * Utility class is to login that connect the authentication center with http
 * client
 *
 * @author Nichole
 *
 *         2015年4月2日
 */
public class LoginHelper {

    private static final Logger logger = LoggerFactory.getLogger(LoginHelper.class);

    public static String login(Authentication auth, UserDto userDto) {

        Map<String, String> params = Maps.newTreeMap();
        params.put("userid", String.valueOf(userDto.getStaffId()));
        params.put("pwd", DigestUtils.md5Hex(userDto.getPassword()));
        params.put("appid", auth.getAppId());
        params.put("timespan", String.valueOf(System.currentTimeMillis() / 1000 + 28800));
        params.put("sign", sign(auth.getAppSecret(), params));

        logger.info("C#用户中心登录, param:{}", params);

        String res = HttpWebClient.post(auth.getAuthUrl(), params);
        logger.info("登录结果:{}", res);

        return res;
    }

    /**
     * to Sign the request parameters with the natural order
     *
     * @param appSecret
     * @param params
     * @return
     */
    private static String sign(String appSecret, Map<String, String> params) {
        boolean firstParam = true;
        StringBuilder sb = new StringBuilder();
        Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, String> item = iterator.next();
            if (!firstParam) {
                sb.append("&");
            }
            sb.append(item.getKey()).append("=").append(item.getValue());
            firstParam = false;
        }
        sb.append("&&appsecret=").append(appSecret);
        return DigestUtils.md5Hex(sb.toString());
    }

    /**
     * retrieve privilege list by user id
     *
     * @param auth
     * @param userId
     * @return
     */
    public static String checkUserPrivilege(Authentication auth, String userId) {
        Map<String, String> params = new TreeMap<String, String>();
        params.put("userid", userId);
        params.put("syscode", auth.getPrivilegeRegId());

        logger.debug("Retrieve privilege paramters[{}]", params.toString());

        return HttpWebClient.post(auth.getPrivilegeUrl(), params);
    }

    /**
     * retrieve area privilege list by user id
     *
     * @param auth
     * @param userId
     * @return
     */
    public static String checkAreaPrivilege(Authentication auth, String userId) {
        Map<String, String> params = new TreeMap<String, String>();
        params.put("ygbh", userId);

        logger.debug("Retrieve area privilege paramters[{}]", params.toString());

        return HttpWebClient.post(auth.getAreaPrivilegeUrl(), params);
    }

    /**
     * retrieve shop privilege list by user id
     *
     * @param auth
     * @param userId
     * @return
     */
    public static String checkShopPrivilege(Authentication auth, String userId) {
        Map<String, String> params = new TreeMap<String, String>();
        params.put("ygbh", userId);
        params.put("appid", auth.getAppId());
        params.put("timespan", String.valueOf(System.currentTimeMillis() / 1000 + 28800));
        params.put("sign", sign(auth.getAppSecret(), params));

        logger.debug("Retrieve shop privilege paramters[{}]", params.toString());

        return HttpWebClient.post(auth.getShopPrivilegeUrl(), params);
    }
}
