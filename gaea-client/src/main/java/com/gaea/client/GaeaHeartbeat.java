package com.gaea.client;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gaea.client.service.GaeaRemoteService;
import com.google.common.collect.Maps;

/**
 * Created by chengpanwang on 4/20/16.
 */
public class GaeaHeartbeat {

    private static final Logger logger = LoggerFactory.getLogger(GaeaHeartbeat.class);

    private GaeaRemoteService   gaeaRemoteService;

    public GaeaHeartbeat(GaeaRemoteService gaeaRemoteService) {
        this.gaeaRemoteService = gaeaRemoteService;
    }

    public void start() {
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        TimeUnit.SECONDS.sleep(60);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) GaeaContext.getSecurityManager();
                    DefaultSessionManager sessionManager = (DefaultSessionManager) securityManager.getSessionManager();
                    Collection<Session> activeSessions = sessionManager.getSessionDAO().getActiveSessions();
                    if (CollectionUtils.isEmpty(activeSessions)) {
                        continue;
                    }

                    Map<String, Long> sessionStatusMap = Maps.newHashMap();
                    for (Session session : activeSessions) {
                        sessionStatusMap.put((String) session.getAttribute(Constants.TICKET_NAME), session.getLastAccessTime().getTime());
                    }

                    gaeaRemoteService.sync(sessionStatusMap);

                }
            }
        };

        new Thread(runnable, "gaea_heartbeat").start();

    }

}
