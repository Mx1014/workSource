package com.everhomes.pusher;

import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.OkHttpClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.everhomes.apnshttp2.builder.ApnsClient;
import com.everhomes.apnshttp2.notifiction.Notification;
import com.everhomes.apnshttp2.notifiction.NotificationResponse;
import com.everhomes.apnshttp2.notifiction.NotificationResponseListener;
import com.notnoop.exceptions.NetworkIOException;

public class PriorityQueuedApnsClient implements ApnsClient {

    private static final Logger logger = LoggerFactory.getLogger(PriorityQueuedApnsClient.class);
    
    private ApnsClient client;
    private PriorityBlockingQueue<Notification> queue;
    private AtomicBoolean started = new AtomicBoolean(false);
    private final ThreadFactory threadFactory;
    private Thread thread;
    private volatile boolean shouldContinue;

    public PriorityQueuedApnsClient(ApnsClient client) {
        this(client, null);
    }

    public PriorityQueuedApnsClient(ApnsClient client, final ThreadFactory tf) {
        this.client = client;
        this.queue = new PriorityBlockingQueue<Notification>();
        this.threadFactory = tf == null ? Executors.defaultThreadFactory() : tf;
        this.thread = null;
    }

    public void addPush(Notification msg) {
        if (!started.get()) {
            throw new IllegalStateException("client hasn't be started or was closed");
        }
        queue.add(msg);
    }

    public void start() {
        if (started.getAndSet(true)) {
            return;
        }

        //client.start(); the real client is already started
        shouldContinue = true;
        thread = threadFactory.newThread(new Runnable() {
            public void run() {
                while (shouldContinue) {
                    try {
                    	Notification msg = queue.take();
                    	if(msg != null){
                    		NotificationResponse result = client.push(msg);
	                       	 //推送失败,打印失败日志
	                       	 if(result == null || result.getHttpStatusCode() != 200){
	                       		 //queue.add(msg);
	                       		logger.error("Pushing message failure ,NotificationResponse= "+result+"  priority=" + msg.getPriority());
	                       	 }
	                           logger.info("Pushing message,NotificationResponse= "+result+"  priority=" + msg.getPriority());
	                           logger.info("NotificationResponse:"+result);
                    	}
                    	 
                    } catch (InterruptedException e) {
                        // ignore
                    } catch (NetworkIOException e) {
                        // ignore: failed connect...
                    } catch (Exception e) {
                        logger.warn("Unexpected message caught... Shouldn't be here", e);
                    }
                }
            }
        });
        thread.start();
    }

    public void stop() {
        started.set(false);
        shouldContinue = false;
        thread.interrupt();
        shutdown();
    }    

	@Override
	public boolean isSynchronous() {
		
		if(client != null){
			return client.isSynchronous();
		}
		return false;
	}

	@Override
	public void push(Notification notification,
			NotificationResponseListener listener) {
		if(client != null){
			client.push(notification,listener);
		}
		
	}

	@Override
	public NotificationResponse push(Notification notification) {
		if(client == null){
			return null ;
		}
		return client.push(notification);
	}

	@Override
	public OkHttpClient getHttpClient() {
		if(client == null){
			return null ;
		}
		return client.getHttpClient();
	}

	@Override
	public void shutdown() {
		
		if(client != null){
			client.shutdown();
		}
	}

    @Override
    public String toString() {
        return client.toString();
    }

}
