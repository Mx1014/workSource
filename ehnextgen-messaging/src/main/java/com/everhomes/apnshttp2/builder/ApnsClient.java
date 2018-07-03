
package com.everhomes.apnshttp2.builder;

import com.everhomes.apnshttp2.notifiction.Notification;
import com.everhomes.apnshttp2.notifiction.NotificationResponse;
import com.everhomes.apnshttp2.notifiction.NotificationResponseListener;

import okhttp3.OkHttpClient;

/**
 * apnshttp2 包下的类来自https://github.com/CleverTap/apns-http2 ，
 * 我只是个代码的搬运工
 * @author huanglm
 *
 */
public interface ApnsClient {

    /**
     * Checks whether the client supports synchronous operations.
     * <p>
     * This is specified when building the client using
     *
     * @return Whether the client supports synchronous operations
     */
    boolean isSynchronous();

    /**
     * Sends a notification asynchronously to the Apple Push Notification Service.
     *
     * @param notification The notification built using
     *                     {@link Notification.Builder}
     * @param listener     The listener to be called after the request is complete
     */
    void push(Notification notification, NotificationResponseListener listener);

    /**
     * Sends a notification synchronously to the Apple Push Notification Service.
     *
     * @param notification The notification built using
     *                     {@link Notification.Builder}
     * @return The notification response
     */
    NotificationResponse push(Notification notification);

    /**
     * Returns the underlying OkHttpClient instance.
     * This can be used for further customizations such as using proxies.
     *
     * @return The underlying OkHttpClient instance
     */
    OkHttpClient getHttpClient();
    
    /**
     * 关闭client
     */
     void shutdown() ;
     
     /**
      * 启动线程从队列中取信息推送
      */
      void start();
      
      /**
       * 将推送消息添加到队列
       * @param msg
       */     
      void addPush(Notification msg);
      
      /**
       * 停止推送客户端
       */
       void stop();
}
