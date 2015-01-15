package com.everhomes.client;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketClient;
import org.eclipse.jetty.websocket.WebSocketClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rpc.HeartbeatPdu;
import com.everhomes.rpc.PduFrame;
import com.everhomes.rpc.client.RegisterConnectionRequestPdu;
import com.everhomes.user.LogonCommandResponse;
import com.everhomes.util.ConsumerCallback;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RandomGenerator;
import com.google.gson.Gson;

/**
 * Client Java implementation
 * 
 * @author Kelven Yang
 *
 */
public class ClientImpl implements Client {
    private static final Logger LOGGER = LoggerFactory.getLogger( ClientImpl.class);

    // client API
    private Map<Long, List<ClientMessageHandler>> messageHandlers = new HashMap<Long, List<ClientMessageHandler>>();
    private ClientListener listener;
    
    // REST service
    private String serviceUri;
    private CloseableHttpClient httpClient;
    private HttpContext httpClientContext;
    private int restConcurrencyLevel = 4;
    ScheduledExecutorService restExecutor;

    // login management
    enum LoginState { offline, loginInProgress, loggedIn }
    private long uid;
    private String loginToken;
    private List<String> borderList;
    private volatile LoginState loginState = LoginState.offline;
    
    // connection management
    enum ConnectionState { disconnected, connecting, connected }
    private WebSocketClientFactory factory;
    private WebSocket.Connection connection;
    private ScheduledExecutorService connectionExecutor;
    private volatile ConnectionState connectionState = ConnectionState.disconnected;
    private int connectAttemptCount = 0;
    
    // connection heartbeat management
    private ScheduledFuture<?> heartbeatFuture;
    private AtomicLong lastSendTick = new AtomicLong();
    private AtomicLong lastReceiveTick = new AtomicLong();
    private int heartbeatIntervalMs = 10000;
    private int backoffMinMs = 1000;
    private int backoffMaxMs = 5000; 
    private long backoffExpirationTick;
    
    private Gson gson;
    
    public ClientImpl() {
        connectionExecutor = Executors.newScheduledThreadPool(1);
        factory = new WebSocketClientFactory();
        gson = new Gson();
    
        // register system controller
        registerMessageHandler(new long[] { Constants.APPID_SYSTEM }, new ControlMessageHandler());
    }
    
    @Override
    public int getHeartbeatIntervalMs() {
        return this.heartbeatIntervalMs;
    }
    
    @Override
    public void setHeartbeatInervalMs(int interval) {
        this.heartbeatIntervalMs = interval;
    }
    
    @Override
    public int getBackoffMinMs() {
        return this.backoffMinMs;
    }
    
    @Override
    public void setBackoffMinMs(int value) {
        this.backoffMinMs = value;
    }
    
    @Override
    public int getBackoffMaxMs() {
        return this.backoffMaxMs;
    }
    
    @Override
    public void setBackoffMaxMs(int value) {
        this.backoffMaxMs = value;
    }
    
    @Override
    public int getRestConccurrencyLevel() {
        return this.restConcurrencyLevel;
    }
    
    @Override
    public void setRestConcurrencyLevel(int level) {
        this.restConcurrencyLevel = level;
    }
    
    /**
     * It is not thread-safe, call it in the setup phase
     * 
     * @param appIds applications that the handler can take care of
     * @param handler message handler
     */
    @Override
    public void registerMessageHandler(long[] appIds, ClientMessageHandler handler) {
        assert(appIds != null);
        for(long appId : appIds) {
            List<ClientMessageHandler> handlerPipeline = messageHandlers.get(appId);
            if(handlerPipeline == null) {
                handlerPipeline = new ArrayList<ClientMessageHandler>();
                messageHandlers.put(appId, handlerPipeline);
            }
            
            handlerPipeline.add(handler);
        }
    }
    
    @Override
    public boolean init(String serviceUri, ClientListener listener) {
        assert(listener != null);
        this.listener = listener;
        
        this.serviceUri = serviceUri;
        try {
            factory.start();
            
            this.restExecutor = Executors.newScheduledThreadPool(this.restConcurrencyLevel);
            
            connectionExecutor.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    checkConnection();
                }
            }, 0, 1000, TimeUnit.MILLISECONDS);
            return true;
        } catch (Exception e) {
            LOGGER.error("Unexpected exception", e);
        }
        
        return false;
    }
    
    @Override
    public void shutdown() {
        LOGGER.info("Shutdown is called, forcely switch to offline and perform shutdown");
        setLoginState(LoginState.offline);
        
        if(this.restExecutor != null) {
            LOGGER.info("Shutting down REST executor");
            this.restExecutor.shutdown();
            
            try {
                this.restExecutor.awaitTermination(5000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
            }
            LOGGER.info("REST executor stopped");
        }
        this.connectionExecutor.shutdown();
        
        LOGGER.info("Shutting down connection executor");
        try {
            this.connectionExecutor.awaitTermination(5000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
        }
        LOGGER.info("Connection executor stopped");
    }
    
    @Override
    public boolean logon(String userIdentifier, String password, String deviceIdentifier) {
        LoginState currentState = getLoginState();
        if(currentState == LoginState.offline) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("userIdentifier", userIdentifier);
            params.put("password", password);
            params.put("deviceIdentifier", deviceIdentifier);

            setLoginState(LoginState.loginInProgress);
            LogonRestResponse response = (LogonRestResponse)this.restCall("POST", "/user/logon", params, LogonRestResponse.class);
            if(response.getResponse() != null) {
                LogonCommandResponse cmdResponse = response.getResponse();
                uid = cmdResponse.getUid();
                loginToken = cmdResponse.getLoginToken();
                borderList = cmdResponse.getAccessPoints();
                setBackoff(0);
                setLoginState(LoginState.loggedIn);
                
                // kick connection activity right away
                this.connectionExecutor.execute(new Runnable() {
                    public void run() {
                        checkConnection();
                    }
                });
                return true;
            } else {
                this.listener.onLoginError(response.getErrorScope(), response.getErrorCode(), response.getErrorDescription());
            }
            setLoginState(LoginState.offline);
            return false;
        } else if(currentState == LoginState.loginInProgress) {
            LOGGER.info("Login is already in progress");
            return false;
        }
        return true;
    }
    
    @Override
    public void logon(final String userIdentifier, final String password, final String deviceIdentifier, final ConsumerCallback<Boolean> responseCallback) {
        this.restExecutor.execute(new Runnable() {
            public void run() {
                boolean result = logon(userIdentifier, password, deviceIdentifier);
                responseCallback.consume(result);
            }
        });
    }
    
    @Override
    public boolean logonByToken(String loginToken) {
        LoginState currentState = getLoginState();
        if(currentState == LoginState.offline) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("loginToken", loginToken);

            setLoginState(LoginState.loginInProgress);
            LogonRestResponse response = (LogonRestResponse)this.restCall("POST", "/user/logonByToken", params, LogonRestResponse.class);
            if(response.getResponse() != null) {
                LogonCommandResponse cmdResponse = response.getResponse();
                uid = cmdResponse.getUid();
                loginToken = cmdResponse.getLoginToken();
                borderList = cmdResponse.getAccessPoints();
                setBackoff(0);
                setLoginState(LoginState.loggedIn);
                
                // kick connection activity right away
                kickConnectionCheck();
                return true;
            }
            setLoginState(LoginState.offline);
            return false;
        } else if(currentState == LoginState.loginInProgress) {
            LOGGER.info("Login is already in progress");
            return false;
        }
        return true;
    }
    
    @Override
    public void logonByToken(final String loginToken, final ConsumerCallback<Boolean> responseCallback) {
        this.restExecutor.execute(new Runnable() {
            public void run() {
                boolean result = logonByToken(loginToken);
                responseCallback.consume(result);
            }
        });
    }

    @Override
    public void logoff() {
        if(getLoginState() == LoginState.loggedIn) {
            Map<String, String> params = new HashMap<String, String>();
            StringResultRestResponse response = this.restCall("POST", "/user/logff", params, StringResultRestResponse.class);
            if(response.getResponse() != null && response.getResponse().equals("OK")) {
                setLoginState(LoginState.offline);
                kickConnectionCheck();
            }
        }
    }
    
    @Override
    public long getLoggedUid() {
        return this.uid;
    }
    
    @Override
    public String getLoginToken() {
        return this.loginToken;
    }

    @Override
    public <T extends RestResponseBase> T restCall(String method, String commandRelativeUri, Map<String, String> params, Class<T> responseClz) {
        assert(commandRelativeUri != null);
        
        if(params == null)
            params = new HashMap<String, String>();

        CloseableHttpClient client = openHttpClient();
        String uri = composeFullUri(commandRelativeUri);
        
        try {
            CloseableHttpResponse res;
            if(method.equalsIgnoreCase("POST") || method.equalsIgnoreCase("PUT")) {
                HttpPost post = new HttpPost(uri);
                
                List<NameValuePair> formparams = new ArrayList<NameValuePair>();
                for(Map.Entry<String, String> entry : params.entrySet()) {
                    formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                if(this.loginToken != null)
                    formparams.add(new BasicNameValuePair("token", this.loginToken));
                
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
                post.setEntity(entity);
            
                res = client.execute(post, this.httpClientContext);
            } else {
                RequestBuilder builder = RequestBuilder.create(method.toString()).setUri(uri);
                for(Map.Entry<String, String> entry : params.entrySet()) {
                    builder.addParameter(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                if(this.loginToken != null)
                    builder.addParameter(new BasicNameValuePair("token", this.loginToken));
                
                HttpUriRequest uriRequest = builder.build();
                
                res = client.execute(uriRequest, this.httpClientContext);
            }
            
            try {
                HttpEntity resEntity = res.getEntity();
                String responseBody = EntityUtils.toString(resEntity);
                LOGGER.info("REST call response: " + responseBody);
                return gson.fromJson(responseBody, responseClz);
            } finally {
                res.close();
            }
        } catch(ClientProtocolException e) {
            LOGGER.warn("Unexpected exception", e);
        } catch(IOException e) {
            return ConvertHelper.convert(new RestResponseBase("HTTP", HttpStatus.SC_BAD_GATEWAY, null), responseClz);
        }
        
        return ConvertHelper.convert(new RestResponseBase("HTTP", HttpStatus.SC_BAD_REQUEST, null), responseClz);
    }
    
    @Override
    public <T extends RestResponseBase> void restCall(final String method, final String commandRelativeUri, final Map<String, String> params, final Class<T> responseClz, 
        final ConsumerCallback<T> responseCallback) {
        
        this.restExecutor.execute(new Runnable() {
            public void run() {
                T result = restCall(method, commandRelativeUri, params, responseClz);
                responseCallback.consume(result);
            }
        });
    }
    
    private boolean isHttpClientOpen() {
        return this.httpClient != null;
    }
    
    @SuppressWarnings("unused")
    private void closeHttpClient() {
        if(this.httpClient != null) {
            assert(this.httpClientContext != null);
            try {
                this.httpClient.close();
                
                this.httpClient = null;
                this.httpClientContext = null;
            } catch (IOException e) {
                LOGGER.warn("Unalbe to close", e);
            }
        }
    }
    
    private CloseableHttpClient openHttpClient() {
        if(isHttpClientOpen())
            return this.httpClient;
        
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        this.httpClient = HttpClients.custom().setConnectionManager(cm).build();
        this.httpClientContext = HttpClientContext.create();
        
        if(this.httpClient == null)
            throw new RuntimeException("Unable to create HttpClient object");
        return this.httpClient;
    }
    
    private String composeFullUri(String commandRelativeUri) {
        StringBuffer sb = new StringBuffer(this.serviceUri);
        if(!this.serviceUri.endsWith("/")) {
            sb.append("/");
        }
        if(commandRelativeUri.startsWith("/"))
            sb.append(commandRelativeUri.substring(1));
        else
            sb.append(commandRelativeUri);
        return sb.toString();
    }
    
    private LoginState getLoginState() {
        return this.loginState;
    }
    
    private void setLoginState(LoginState state) {
        if(getLoginState() == state)
            return;
        
        this.loginState = state;
        
        switch(state) {
        case offline:
            listener.onOffline();
            break;
            
        case loginInProgress:
            listener.onLoginInProgress();
            break;
            
        case loggedIn:
            listener.onLoggedIn();
            break;
            
        default :
            assert(false);
            break;
        }
    }
    
    private void heartbeat() {
        long currentTick = DateHelper.currentGMTTime().getTime();
        if(currentTick - this.lastSendTick.get() > this.heartbeatIntervalMs) {
            HeartbeatPdu pdu = new HeartbeatPdu();
            pdu.setLastPeerReceiveTime(this.lastReceiveTick.get());
            
            PduFrame frame = new PduFrame();
            frame.setVersion(Constants.CLIENT_RPC_VERSION);
            frame.setAppId(Constants.APPID_SYSTEM);
            frame.setPayload(pdu);
            sendWebSocketMessage(gson.toJson(frame));
        }
    }
    
    private void kickConnectionCheck() {
        this.connectionExecutor.execute(new Runnable() {
            public void run() {
                checkConnection();
            }
        });
    }
    
    private void checkConnection() {
        switch(getLoginState()) {
        case offline:
            if(getConnectionState() == ConnectionState.connected) {
                LOGGER.info("Client went to offline state. close connection");
                closeConnection();
            }
            break;
            
        case loginInProgress:
            break;
            
        case loggedIn:
            if(getConnectionState() == ConnectionState.disconnected && System.currentTimeMillis() >= this.backoffExpirationTick) {
                String borderAddress = pickAddressToConnect();
                if(borderAddress != null) {
                    final String borderUri = String.format("ws://%s/client", borderAddress);
                    LOGGER.info("Create web socket connection to border " + borderUri);
                    connectAttemptCount++;
                    setConnectionState(ConnectionState.connecting);
                    try {
                        connect(new URI(borderUri));
                    } catch(Exception e) {
                        // TODO
                        LOGGER.error("Unable to establish notification connection");
                        setConnectionState(ConnectionState.disconnected);
                    }
                } else {
                    LOGGER.error("Server is not configured with access borders. We will cease connect retry until your next login");
                    setBackoff(Integer.MAX_VALUE);
                }
            }
            break;
            
        default :
            assert(false);
            break;
        }
    }
    
    private String pickAddressToConnect() {
        if(borderList.size() > 0) {
            Collections.shuffle(borderList);
            
            return borderList.get(0);
        }
        
        return null;
    }
    
    private void setBackoff(int backoffMs) {
        this.backoffExpirationTick = System.currentTimeMillis() + backoffMs;
    }
    
    private void setRandomBackoff() {
        this.backoffExpirationTick = System.currentTimeMillis()
                + RandomGenerator.getRandomNumberBetween(this.backoffMinMs, this.backoffMaxMs);
    }
    
    private void connect(URI uri) throws Exception {
        WebSocketClient client = factory.newWebSocketClient();
        connection = client.open(uri, new WebSocketListener()).get(10, TimeUnit.SECONDS);
    }
    
    private void sendWebSocketMessage(final String message) {
        this.connectionExecutor.execute(new Runnable() {
            public void run() {
                if(ClientImpl.this.connection != null) {
                    try {
                        ClientImpl.this.connection.sendMessage(message);
                        lastSendTick.set(DateHelper.currentGMTTime().getTime());
                    } catch (IOException e) {
                        LOGGER.error("sendMessage() encountered IO exception. message: " + message); 
                    }
                }
            }
        });
    }
    
    private ConnectionState getConnectionState() {
        return this.connectionState;
    }
    
    private void setConnectionState(ConnectionState state) {
        if(getConnectionState() == state)
            return;
        
        this.connectionState = state;
        switch(state) {
        case disconnected:
            LOGGER.info("Connection state -> disconnected");
            listener.onConnectionClose();
            
            if(getLoginState() == LoginState.loggedIn)
                setRandomBackoff();
            break;
            
        case connecting:
            LOGGER.info("Connection state -> connecting, attempt count: " + this.connectAttemptCount);
            listener.onConnectInProgress(this.connectAttemptCount);
            break;
            
        case connected:
            LOGGER.info("Connection state -> connected");
            
            // don't forget to reset counting on the attempts
            this.connectAttemptCount = 0;
            break;
            
        default :
            assert(false);
            break;
        }
    }
    
    private void closeConnection() {
        if(heartbeatFuture != null) {
            heartbeatFuture.cancel(false);
            heartbeatFuture = null;
        }
        
        if(connection != null) {
            connection.close();
            connection = null;
        }
    }
    
    private class WebSocketListener implements WebSocket.OnTextMessage {
        public WebSocketListener() {
        }

        public void onOpen(Connection connection) {
            LOGGER.info("Connection is open");
            
            RegisterConnectionRequestPdu req = new RegisterConnectionRequestPdu(loginToken);
            PduFrame frame = new PduFrame();
            frame.setVersion(Constants.CLIENT_RPC_VERSION);
            frame.setAppId(Constants.APPID_SYSTEM);
            frame.setPayload(req);
            sendWebSocketMessage(gson.toJson(frame));
            
            setConnectionState(ConnectionState.connected);
            
            heartbeatFuture = connectionExecutor.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    heartbeat();
                }
            }, 0L,  heartbeatIntervalMs, TimeUnit.MILLISECONDS);
        }

        public void onClose(int closeCode, String message) {
            LOGGER.info("Connection is closed. close code: " + closeCode + ", message: " + message);

            closeConnection();
            setConnectionState(ConnectionState.disconnected);
        }

        public void onMessage(String data) {
            lastReceiveTick.set(DateHelper.currentGMTTime().getTime());
            
            try {
                PduFrame frame = PduFrame.fromJson(data);
                if(frame != null) {
                    long appId = Constants.APPID_SYSTEM;
                    if(frame.getAppId() != null)
                        appId = frame.getAppId().longValue();
                    List<ClientMessageHandler> handlerPipeline = messageHandlers.get(appId);
                    if(handlerPipeline == null) {
                        LOGGER.warn("Unsupported json message: " + data);
                        return;
                    }
                    
                    for(ClientMessageHandler handler : handlerPipeline) {
                        if(frame == null)
                            break;
                        frame = handler.onClientMessage(frame);
                        if(frame == null) {
                            if(LOGGER.isDebugEnabled())
                                LOGGER.debug("json message: " + data + " was filetered after handler: " + handler.getClass().getName());
                        }
                    }
                } else {
                    LOGGER.warn("Unrecoginized json message: " + data);
                }
            } catch(Throwable e) {
                LOGGER.error("Unexpected exception", e);
            }
        }
    }
}
