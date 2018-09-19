package com.everhomes.eureka;

import com.everhomes.oauth2client.HttpResponseEntity;
import com.everhomes.oauth2client.handler.RestCallTemplate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@EnableConfigurationProperties(EurekaProperties.class)
public class EurekaAutoServiceRegistration implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EurekaAutoServiceRegistration.class);

    private final EurekaProperties eurekaProperties;
    private Gson gson;

    @Value("${spring.application.name}")
    private String applicationName;
    @Value("${server.port:8080}")
    private int serverPort;
    @Value("${CORE_IP:10.1.110.80}")
    private String ipAddress;

    private String instanceId;

    public EurekaAutoServiceRegistration(EurekaProperties eurekaProperties) {
        this.eurekaProperties = eurekaProperties;
        this.gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    }

    /*{
        "instance": {
            "instanceId": "10.1.110.80:core:8888",
            "hostName": "10.1.110.80",
            "app": "CORE",
            "ipAddr": "10.1.110.80",
            "status": "UP",
            "overriddenStatus": "UNKNOWN",
            "port": {
                "$": 8888,
                "@enabled": "true"
            },
            "securePort": {
                "$": 443,
                "@enabled": "false"
            },
            "countryId": 1,
            "dataCenterInfo": {
                "@class": "com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo",
                "name": "MyOwn"
            },
            "leaseInfo": {
                "renewalIntervalInSecs": 30,
                "durationInSecs": 90,
                "registrationTimestamp": 0,
                "lastRenewalTimestamp": 0,
                "evictionTimestamp": 0,
                "serviceUpTimestamp": 0
            },
            "metadata": {
                "management.port": "9090",
                "jmx.port": "44057"
            },
            "homePageUrl": "http://10.1.110.80:9090/",
            "statusPageUrl": "http://10.1.110.80:9090/actuator/info",
            "healthCheckUrl": "http://10.1.110.80:9090/actuator/health",
            "vipAddress": "core",
            "secureVipAddress": "core",
            "isCoordinatingDiscoveryServer": "false",
            "lastUpdatedTimestamp": "1537173075523",
            "lastDirtyTimestamp": "1537173275495"
        }
    }*/
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() != null) {
            return;
        }
        this.instanceId = String.format("%s:%s:%s", ipAddress, applicationName, serverPort);

        if (eurekaProperties != null && eurekaProperties.isEnabled()) {
            register();
            addVMHook();
        } else {
            LOGGER.warn("Eureka auto registration disabled");
        }
    }

    private void addVMHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            InstanceInfo instanceInfo = instanceInfoBuilder(InstanceInfo.InstanceStatus.DOWN).build();
            postInstanceInfo(instanceInfo);
        }));
    }

    private void postInstanceInfo(InstanceInfo instanceInfo) {
        try {
            String instanceInfoJson = gson.toJson(new Instance(instanceInfo));
            LOGGER.info("Eureka registration: \n{}", instanceInfoJson);

            String url = UriComponentsBuilder.fromHttpUrl(eurekaProperties.getServiceUrl())
                    .pathSegment("apps", applicationName)
                    .toUriString();

            RestCallTemplate
                    .url(url)
                    .header("Content-Type", "application/json")
                    .respType(String.class)
                    .body(instanceInfoJson)
                    .post();
        } catch (Exception e) {
            LOGGER.error("Eureka registration fail", e);
        }
    }

    private void getOrRegister() {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(eurekaProperties.getServiceUrl())
                    .pathSegment("apps", applicationName.toUpperCase(), instanceId)
                    .toUriString();

            HttpResponseEntity<Instance> response = RestCallTemplate
                    .url(url)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .onError(e -> {
                        if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                            throw new InstanceNotFoundException();
                        }
                    })
                    .respType(Instance.class)
                    .get();

            Instance instance = response.getBody();
            if (instance.getInstance().getStatus() != InstanceInfo.InstanceStatus.UP) {
                LOGGER.info("Instance status is [{}], REGISTER IT", instance.getInstance().getStatus());
                register();
            }
        } catch (InstanceNotFoundException e) {
            LOGGER.warn("Instance not found in eureka, REGISTER IT");
            register();
        } catch (Exception e) {
            LOGGER.error("Get instance from eureka fail", e);
            throw e;
        }
    }

    private void register() {
        InstanceInfo instanceInfo = instanceInfoBuilder(InstanceInfo.InstanceStatus.UP).build();
        postInstanceInfo(instanceInfo);
    }

    private InstanceInfo.Builder instanceInfoBuilder(InstanceInfo.InstanceStatus status) {
        return InstanceInfo.Builder.newBuilder()
                .setInstanceId(instanceId)
                .setAppName(applicationName.toUpperCase())
                .setIPAddr(ipAddress)
                .setHostName(ipAddress)
                .setStatus(status)
                .setPort(new InstanceInfo.PortWrapper("true", serverPort))
                .setSecurePort(new InstanceInfo.PortWrapper("false", 443))
                .setDataCenterInfo(new DataCenterInfo(DataCenterInfo.Name.MyOwn))
                .setLeaseInfo(
                        LeaseInfo.Builder.newBuilder()
                                .setRenewalIntervalInSecs(30)
                                .setDurationInSecs(90)
                                .setEvictionTimestamp(0)
                                .setServiceUpTimestamp(0)
                                .build()
                )
                .setHomePageUrl("/")
                .setStatusPageUrl("/actuator/info")
                .setHealthCheckUrls("/actuator/health")
                .setVIPAddress(applicationName)
                .setSecureVIPAddress(applicationName)
                .setLastUpdatedTimestamp(System.currentTimeMillis())
                .setLastDirtyTimestamp(System.currentTimeMillis() + 1000 * 60 * 3);
    }

    @Scheduled(fixedDelay = 10 * 1000)
    public void heartbeat() {
        try {
            getOrRegister();

            String url = UriComponentsBuilder.fromHttpUrl(eurekaProperties.getServiceUrl())
                    .pathSegment("apps", applicationName.toUpperCase(), instanceId)
                    .queryParam("status", "UP")
                    .queryParam("lastDirtyTimestamp", System.currentTimeMillis())
                    .toUriString();

            RestCallTemplate
                    .url(url)
                    .header("Content-Type", "application/json")
                    .respType(String.class)
                    .method("PUT")
                    .execute();
            LOGGER.info("Eureka heartbeat success [{}] ", url);
        } catch (Exception e) {
            LOGGER.error("Eureka heartbeat fail", e);
        }
    }

    private static class Instance {
        private InstanceInfo instance;

        Instance(InstanceInfo instance) {
            this.instance = instance;
        }

        public InstanceInfo getInstance() {
            return instance;
        }

        public void setInstance(InstanceInfo instance) {
            this.instance = instance;
        }
    }
}
