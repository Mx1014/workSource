package com.everhomes.eureka;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class InstanceInfo {

    public static class PortWrapper {
        @SerializedName("@enabled")
        private final String enabled;
        @SerializedName("$")
        private final int port;

        @JsonCreator
        public PortWrapper(@JsonProperty("@enabled") String enabled, @JsonProperty("$") int port) {
            this.enabled = enabled;
            this.port = port;
        }

        public String isEnabled() {
            return enabled;
        }

        public int getPort() {
            return port;
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(InstanceInfo.class);

    public static final int DEFAULT_PORT = 7001;
    public static final int DEFAULT_SECURE_PORT = 7002;
    public static final int DEFAULT_COUNTRY_ID = 1; // US

    // The (fixed) instanceId for this instanceInfo. This should be unique within the scope of the appName.
    private volatile String instanceId;

    @SerializedName("app")
    private volatile String appName;
    private volatile String appGroupName;

    private volatile String ipAddr;

    private static final String SID_DEFAULT = "na";

    private volatile PortWrapper port;
    private volatile PortWrapper securePort;

    private volatile String homePageUrl;
    private volatile String statusPageUrl;
    private volatile String healthCheckUrl;
    private volatile String secureHealthCheckUrl;
    private volatile String vipAddress;
    private volatile String secureVipAddress;
    private String statusPageRelativeUrl;
    private String statusPageExplicitUrl;
    private String healthCheckRelativeUrl;
    private String healthCheckSecureExplicitUrl;
    private String vipAddressUnresolved;
    private String secureVipAddressUnresolved;
    private String healthCheckExplicitUrl;
    private volatile boolean isSecurePortEnabled = false;
    private volatile boolean isUnsecurePortEnabled = true;
    private volatile DataCenterInfo dataCenterInfo;
    private volatile String hostName;
    private volatile InstanceStatus status = InstanceStatus.UP;
    private volatile InstanceStatus overriddenStatus = InstanceStatus.UNKNOWN;
    private volatile boolean isInstanceInfoDirty = false;
    private volatile LeaseInfo leaseInfo;
    private volatile String isCoordinatingDiscoveryServer = Boolean.FALSE.toString();
    private volatile Map<String, String> metadata;
    private volatile Long lastUpdatedTimestamp;
    private volatile Long lastDirtyTimestamp;
    private volatile ActionType actionType;
    private volatile String asgName;

    private InstanceInfo() {
        this.metadata = new ConcurrentHashMap<String, String>();
        this.lastUpdatedTimestamp = System.currentTimeMillis();
        this.lastDirtyTimestamp = lastUpdatedTimestamp;
    }

    /**
     * shallow copy constructor.
     *
     * @param ii The object to copy
     */
    public InstanceInfo(InstanceInfo ii) {
        this.instanceId = ii.instanceId;
        this.appName = ii.appName;
        this.appGroupName = ii.appGroupName;
        this.ipAddr = ii.ipAddr;

        this.port = ii.port;
        this.securePort = ii.securePort;

        this.homePageUrl = ii.homePageUrl;
        this.statusPageUrl = ii.statusPageUrl;
        this.healthCheckUrl = ii.healthCheckUrl;
        this.secureHealthCheckUrl = ii.secureHealthCheckUrl;

        this.vipAddress = ii.vipAddress;
        this.secureVipAddress = ii.secureVipAddress;
        this.statusPageRelativeUrl = ii.statusPageRelativeUrl;
        this.statusPageExplicitUrl = ii.statusPageExplicitUrl;

        this.healthCheckRelativeUrl = ii.healthCheckRelativeUrl;
        this.healthCheckSecureExplicitUrl = ii.healthCheckSecureExplicitUrl;

        this.vipAddressUnresolved = ii.vipAddressUnresolved;
        this.secureVipAddressUnresolved = ii.secureVipAddressUnresolved;

        this.healthCheckExplicitUrl = ii.healthCheckExplicitUrl;

        this.isSecurePortEnabled = ii.isSecurePortEnabled;
        this.isUnsecurePortEnabled = ii.isUnsecurePortEnabled;

        this.dataCenterInfo = ii.dataCenterInfo;

        this.hostName = ii.hostName;

        this.status = ii.status;
        this.overriddenStatus = ii.overriddenStatus;

        this.isInstanceInfoDirty = ii.isInstanceInfoDirty;

        this.leaseInfo = ii.leaseInfo;

        this.isCoordinatingDiscoveryServer = ii.isCoordinatingDiscoveryServer;

        this.metadata = ii.metadata;

        this.lastUpdatedTimestamp = ii.lastUpdatedTimestamp;
        this.lastDirtyTimestamp = ii.lastDirtyTimestamp;

        this.actionType = ii.actionType;

        this.asgName = ii.asgName;
    }


    public enum InstanceStatus {
        UP, // Ready to receive traffic
        DOWN, // Do not send traffic- healthcheck callback failed
        STARTING, // Just about starting- initializations to be done - do not
        // send traffic
        OUT_OF_SERVICE, // Intentionally shutdown for traffic
        UNKNOWN;

        public static InstanceInfo.InstanceStatus toEnum(String s) {
            if (s != null) {
                try {
                    return InstanceInfo.InstanceStatus.valueOf(s.toUpperCase());
                } catch (IllegalArgumentException e) {
                    // ignore and fall through to unknown
                    logger.debug("illegal argument supplied to InstanceStatus.valueOf: {}, defaulting to {}", s, UNKNOWN);
                }
            }
            return UNKNOWN;
        }
    }

    @Override
    public int hashCode() {
        String id = getInstanceId();
        return (id == null) ? 31 : (id.hashCode() + 31);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        InstanceInfo other = (InstanceInfo) obj;
        String id = getInstanceId();
        if (id == null) {
            if (other.getInstanceId() != null) {
                return false;
            }
        } else if (!id.equals(other.getInstanceId())) {
            return false;
        }
        return true;
    }

    public enum PortType {
        SECURE, UNSECURE
    }

    public static final class Builder {
        private static final String COLON = ":";
        private static final String HTTPS_PROTOCOL = "https://";
        private static final String HTTP_PROTOCOL = "http://";
        private final Function<String, String> intern = String::intern;

        private InstanceInfo result = new InstanceInfo();

        private String namespace;


        public static InstanceInfo.Builder newBuilder() {
            return new InstanceInfo.Builder();
        }

        public InstanceInfo.Builder setInstanceId(String instanceId) {
            result.instanceId = instanceId;
            return this;
        }

        /**
         * Set the application name of the instance.This is mostly used in
         * querying of instances.
         *
         * @param appName the application name.
         * @return the instance info builder.
         */
        public InstanceInfo.Builder setAppName(String appName) {
            result.appName = intern.apply(appName.toUpperCase(Locale.ROOT));
            return this;
        }

        public InstanceInfo.Builder setAppNameForDeser(String appName) {
            result.appName = appName;
            return this;
        }


        public InstanceInfo.Builder setAppGroupName(String appGroupName) {
            if (appGroupName != null) {
                result.appGroupName = intern.apply(appGroupName.toUpperCase(Locale.ROOT));
            } else {
                result.appGroupName = null;
            }
            return this;
        }

        public InstanceInfo.Builder setAppGroupNameForDeser(String appGroupName) {
            result.appGroupName = appGroupName;
            return this;
        }

        /**
         * Sets the fully qualified hostname of this running instance.This is
         * mostly used in constructing the {@link java.net.URL} for communicating with
         * the instance.
         *
         * @param hostName the host name of the instance.
         * @return the {@link InstanceInfo} builder.
         */
        public InstanceInfo.Builder setHostName(String hostName) {
            if (hostName == null || hostName.isEmpty()) {
                logger.warn("Passed in hostname is blank, not setting it");
                return this;
            }

            String existingHostName = result.hostName;
            result.hostName = hostName;
            return this;
        }

        /**
         * Sets the status of the instances.If the status is UP, that is when
         * the instance is ready to service requests.
         *
         * @param status the {@link InstanceInfo.InstanceStatus} of the instance.
         * @return the {@link InstanceInfo} builder.
         */
        public InstanceInfo.Builder setStatus(InstanceInfo.InstanceStatus status) {
            result.status = status;
            return this;
        }

        /**
         * Sets the status overridden by some other external process.This is
         * mostly used in putting an instance out of service to block traffic to
         * it.
         *
         * @param status the overridden {@link InstanceInfo.InstanceStatus} of the instance.
         * @return @return the {@link InstanceInfo} builder.
         */
        public InstanceInfo.Builder setOverriddenStatus(InstanceInfo.InstanceStatus status) {
            result.overriddenStatus = status;
            return this;
        }

        /**
         * Sets the ip address of this running instance.
         *
         * @param ip the ip address of the instance.
         * @return the {@link InstanceInfo} builder.
         */
        public InstanceInfo.Builder setIPAddr(String ip) {
            result.ipAddr = ip;
            return this;
        }

        /**
         * Sets the port number that is used to service requests.
         *
         * @param port the port number that is used to service requests.
         * @return the {@link InstanceInfo} builder.
         */
        public InstanceInfo.Builder setPort(PortWrapper port) {
            result.port = port;
            return this;
        }

        /**
         * Sets the secure port that is used to service requests.
         *
         * @param port the secure port that is used to service requests.
         * @return the {@link InstanceInfo} builder.
         */
        public InstanceInfo.Builder setSecurePort(PortWrapper port) {
            result.securePort = port;
            return this;
        }

        /**
         * Enabled or disable secure/non-secure ports .
         *
         * @param type      Secure or Non-Secure.
         * @param isEnabled true if enabled.
         * @return the instance builder.
         */
        public InstanceInfo.Builder enablePort(InstanceInfo.PortType type, boolean isEnabled) {
            if (type == InstanceInfo.PortType.SECURE) {
                result.isSecurePortEnabled = isEnabled;
            } else {
                result.isUnsecurePortEnabled = isEnabled;
            }
            return this;
        }

        public InstanceInfo.Builder setHomePageUrl(String relativeUrl) {
            result.homePageUrl = HTTP_PROTOCOL + result.hostName + COLON
                    + result.port.port;
            if (relativeUrl != null) {
                result.homePageUrl += relativeUrl;
            }
            return this;
        }

        public InstanceInfo.Builder setStatusPageUrl(String relativeUrl) {
            result.statusPageUrl = HTTP_PROTOCOL + result.hostName + COLON
                    + result.port.port + relativeUrl;
            return this;
        }

        /**
         * Sets the absolute health check {@link java.net.URL} for this instance for both
         * secure and non-secure communication The users can provide the
         * <code>healthCheckUrlPath</code> if the healthcheck page resides in
         * the same instance talking to discovery, else in the cases where the
         * instance is a proxy for some other server, it can provide the full
         * {@link java.net.URL}. If the full {@link java.net.URL} is provided it takes precedence.
         * <p>
         * <p>
         * The full {@link java.net.URL} should follow the format
         * http://${netflix.appinfo.hostname}:7001/healthcheck where the value
         * ${netflix.appinfo.hostname} is replaced at runtime.
         * </p>
         *
         * @param relativeUrl       - The {@link java.net.URL} path for healthcheck page for this
         *                          instance.
         */
        public InstanceInfo.Builder setHealthCheckUrls(String relativeUrl) {
            result.healthCheckUrl = HTTP_PROTOCOL + result.hostName + COLON
                    + result.port.port + relativeUrl;
            return this;
        }

        /**
         * Sets the Virtual Internet Protocol address for this instance. The
         * address should follow the format <code><hostname:port></code> This
         * address needs to be resolved into a real address for communicating
         * with this instance.
         *
         * @param vipAddress - The Virtual Internet Protocol address of this instance.
         * @return the instance builder.
         */
        public InstanceInfo.Builder setVIPAddress(final String vipAddress) {
            result.vipAddressUnresolved = intern.apply(vipAddress);
            result.vipAddress = intern.apply(vipAddress);
            return this;
        }

        /**
         * Setter used during deserialization process, that does not do macro expansion on the provided value.
         */
        public InstanceInfo.Builder setVIPAddressDeser(String vipAddress) {
            result.vipAddress = intern.apply(vipAddress);
            return this;
        }

        /**
         * Sets the Secure Virtual Internet Protocol address for this instance.
         * The address should follow the format <hostname:port> This address
         * needs to be resolved into a real address for communicating with this
         * instance.
         *
         * @param secureVIPAddress the secure VIP address of the instance.
         * @return - Builder instance
         */
        public InstanceInfo.Builder setSecureVIPAddress(final String secureVIPAddress) {
            result.secureVipAddressUnresolved = intern.apply(secureVIPAddress);
            result.secureVipAddress = intern.apply(secureVIPAddress);
            return this;
        }

        /**
         * Setter used during deserialization process, that does not do macro expansion on the provided value.
         */
        public InstanceInfo.Builder setSecureVIPAddressDeser(String secureVIPAddress) {
            result.secureVipAddress = intern.apply(secureVIPAddress);
            return this;
        }

        /**
         * Sets the datacenter information.
         *
         * @param datacenter the datacenter information for where this instance is
         *                   running.
         * @return the {@link InstanceInfo} builder.
         */
        public InstanceInfo.Builder setDataCenterInfo(DataCenterInfo datacenter) {
            result.dataCenterInfo = datacenter;
            return this;
        }

        /**
         * Set the instance lease information.
         *
         * @param info the lease information for this instance.
         */
        public InstanceInfo.Builder setLeaseInfo(LeaseInfo info) {
            result.leaseInfo = info;
            return this;
        }

        /**
         * Add arbitrary metadata to running instance.
         *
         * @param key The key of the metadata.
         * @param val The value of the metadata.
         * @return the {@link InstanceInfo} builder.
         */
        public InstanceInfo.Builder add(String key, String val) {
            result.metadata.put(key, val);
            return this;
        }

        /**
         * Replace the existing metadata map with a new one.
         *
         * @param mt the new metadata name.
         * @return instance info builder.
         */
        public InstanceInfo.Builder setMetadata(Map<String, String> mt) {
            result.metadata = mt;
            return this;
        }

        /**
         * Returns the encapsulated instance info even it it is not built fully.
         *
         * @return the existing information about the instance.
         */
        public InstanceInfo getRawInstance() {
            return result;
        }

        /**
         * Build the {@link InstanceInfo} object.
         *
         * @return the {@link InstanceInfo} that was built based on the
         * information supplied.
         */
        public InstanceInfo build() {
            if (!isInitialized()) {
                throw new IllegalStateException("name is required!");
            }
            return result;
        }

        public boolean isInitialized() {
            return (result.appName != null);
        }

        /**
         * Sets the AWS ASG name for this instance.
         *
         * @param asgName the asg name for this instance.
         * @return the instance info builder.
         */
        public InstanceInfo.Builder setASGName(String asgName) {
            result.asgName = intern.apply(asgName);
            return this;
        }

        private InstanceInfo.Builder refreshSecureVIPAddress() {
            setSecureVIPAddress(result.secureVipAddressUnresolved);
            return this;
        }

        private InstanceInfo.Builder refreshVIPAddress() {
            setVIPAddress(result.vipAddressUnresolved);
            return this;
        }

        public InstanceInfo.Builder setLastUpdatedTimestamp(long lastUpdatedTimestamp) {
            result.lastUpdatedTimestamp = lastUpdatedTimestamp;
            return this;
        }

        public InstanceInfo.Builder setLastDirtyTimestamp(long lastDirtyTimestamp) {
            result.lastDirtyTimestamp = lastDirtyTimestamp;
            return this;
        }

        public InstanceInfo.Builder setActionType(InstanceInfo.ActionType actionType) {
            result.actionType = actionType;
            return this;
        }

        public InstanceInfo.Builder setNamespace(String namespace) {
            this.namespace = namespace.endsWith(".")
                    ? namespace
                    : namespace + ".";
            return this;
        }
    }

    public String getInstanceId() {
        return instanceId;
    }

    /**
     * Return the name of the application registering with discovery.
     *
     * @return the string denoting the application name.
     */
    public String getAppName() {
        return appName;
    }

    public String getAppGroupName() {
        return appGroupName;
    }


    public String getHostName() {
        return hostName;
    }

    /**
     * Returns the ip address of the instance.
     *
     * @return - the ip address, in AWS scenario it is a private IP.
     */
    @JsonProperty("ipAddr")
    public String getIPAddr() {
        return ipAddr;
    }

    /**
     * Returns the port number that is used for servicing requests.
     *
     * @return - the non-secure port number.
     */
    @JsonIgnore
    public PortWrapper getPort() {
        return port;
    }

    /**
     * Returns the status of the instance.
     *
     * @return the status indicating whether the instance can handle requests.
     */
    public InstanceInfo.InstanceStatus getStatus() {
        return status;
    }

    /**
     * Returns the overridden status if any of the instance.
     *
     * @return the status indicating whether an external process has changed the
     * status.
     */
    public InstanceInfo.InstanceStatus getOverriddenStatus() {
        return overriddenStatus;
    }

    /**
     * Returns data center information identifying if it is AWS or not.
     *
     * @return the data center information.
     */
    public DataCenterInfo getDataCenterInfo() {
        return dataCenterInfo;
    }

    /**
     * Returns the lease information regarding when it expires.
     *
     * @return the lease information of this instance.
     */
    public LeaseInfo getLeaseInfo() {
        return leaseInfo;
    }

    /**
     * Sets the lease information regarding when it expires.
     *
     * @param info the lease information of this instance.
     */
    public void setLeaseInfo(LeaseInfo info) {
        leaseInfo = info;
    }

    /**
     * Returns all application specific metadata set on the instance.
     *
     * @return application specific metadata.
     */
    public Map<String, String> getMetadata() {
        return metadata;
    }

    /**
     * Returns the secure port that is used for servicing requests.
     *
     * @return the secure port.
     */
    @JsonIgnore
    public PortWrapper getSecurePort() {
        return securePort;
    }

    /**
     * Checks whether a port is enabled for traffic or not.
     *
     * @param type indicates whether it is secure or non-secure port.
     * @return true if the port is enabled, false otherwise.
     */
    @JsonIgnore
    public boolean isPortEnabled(InstanceInfo.PortType type) {
        if (type == InstanceInfo.PortType.SECURE) {
            return isSecurePortEnabled;
        } else {
            return isUnsecurePortEnabled;
        }
    }

    /**
     * Returns the time elapsed since epoch since the instance status has been
     * last updated.
     *
     * @return the time elapsed since epoch since the instance has been last
     * updated.
     */
    public long getLastUpdatedTimestamp() {
        return lastUpdatedTimestamp;
    }

    /**
     * Set the update time for this instance when the status was update.
     */
    public void setLastUpdatedTimestamp() {
        this.lastUpdatedTimestamp = System.currentTimeMillis();
    }

    /**
     * Gets the home page {@link java.net.URL} set for this instance.
     *
     * @return home page {@link java.net.URL}
     */
    public String getHomePageUrl() {
        return homePageUrl;
    }

    /**
     * Gets the status page {@link java.net.URL} set for this instance.
     *
     * @return status page {@link java.net.URL}
     */
    public String getStatusPageUrl() {
        return statusPageUrl;
    }

    /**
     * Gets the absolute URLs for the health check page for both secure and
     * non-secure protocols. If the port is not enabled then the URL is
     * excluded.
     *
     * @return A Set containing the string representation of health check urls
     * for secure and non secure protocols
     */
    @JsonIgnore
    public Set<String> getHealthCheckUrls() {
        Set<String> healthCheckUrlSet = new LinkedHashSet<String>();
        if (this.isUnsecurePortEnabled && healthCheckUrl != null && !healthCheckUrl.isEmpty()) {
            healthCheckUrlSet.add(healthCheckUrl);
        }
        if (this.isSecurePortEnabled && secureHealthCheckUrl != null && !secureHealthCheckUrl.isEmpty()) {
            healthCheckUrlSet.add(secureHealthCheckUrl);
        }
        return healthCheckUrlSet;
    }

    public String getHealthCheckUrl() {
        return healthCheckUrl;
    }

    public String getSecureHealthCheckUrl() {
        return secureHealthCheckUrl;
    }

    /**
     * Gets the Virtual Internet Protocol address for this instance. Defaults to
     * hostname if not specified.
     *
     * @return - The Virtual Internet Protocol address
     */
    @JsonProperty("vipAddress")
    public String getVIPAddress() {
        return vipAddress;
    }

    /**
     * Get the Secure Virtual Internet Protocol address for this instance.
     * Defaults to hostname if not specified.
     *
     * @return - The Secure Virtual Internet Protocol address.
     */
    public String getSecureVipAddress() {
        return secureVipAddress;
    }

    /**
     * Gets the last time stamp when this instance was touched.
     *
     * @return last timestamp when this instance was touched.
     */
    public Long getLastDirtyTimestamp() {
        return lastDirtyTimestamp;
    }

    /**
     * Set the time indicating that the instance was touched.
     *
     * @param lastDirtyTimestamp time when the instance was touched.
     */
    public void setLastDirtyTimestamp(Long lastDirtyTimestamp) {
        this.lastDirtyTimestamp = lastDirtyTimestamp;
    }

    /**
     * Set the status for this instance.
     *
     * @param status status for this instance.
     * @return the prev status if a different status from the current was set, null otherwise
     */
    public synchronized InstanceInfo.InstanceStatus setStatus(InstanceInfo.InstanceStatus status) {
        if (this.status != status) {
            InstanceInfo.InstanceStatus prev = this.status;
            this.status = status;
            setIsDirty();
            return prev;
        }
        return null;
    }

    /**
     * Set the status for this instance without updating the dirty timestamp.
     *
     * @param status status for this instance.
     */
    public synchronized void setStatusWithoutDirty(InstanceInfo.InstanceStatus status) {
        if (this.status != status) {
            this.status = status;
        }
    }

    /**
     * Sets the overridden status for this instance.Normally set by an external
     * process to disable instance from taking traffic.
     *
     * @param status overridden status for this instance.
     */
    public synchronized void setOverriddenStatus(InstanceInfo.InstanceStatus status) {
        if (this.overriddenStatus != status) {
            this.overriddenStatus = status;
        }
    }

    @JsonIgnore
    public boolean isDirty() {
        return isInstanceInfoDirty;
    }

    /**
     * @return the lastDirtyTimestamp if is dirty, null otherwise.
     */
    public synchronized Long isDirtyWithTime() {
        if (isInstanceInfoDirty) {
            return lastDirtyTimestamp;
        } else {
            return null;
        }
    }

    /**
     * @param isDirty true if dirty, false otherwise.
     * @deprecated use {@link #setIsDirty()} and {@link #unsetIsDirty(long)} to set and unset
     * <p>
     * Sets the dirty flag so that the instance information can be carried to
     * the discovery server on the next heartbeat.
     */
    @Deprecated
    public synchronized void setIsDirty(boolean isDirty) {
        if (isDirty) {
            setIsDirty();
        } else {
            isInstanceInfoDirty = false;
            // else don't update lastDirtyTimestamp as we are setting isDirty to false
        }
    }

    /**
     * Sets the dirty flag so that the instance information can be carried to
     * the discovery server on the next heartbeat.
     */
    public synchronized void setIsDirty() {
        isInstanceInfoDirty = true;
        lastDirtyTimestamp = System.currentTimeMillis();
    }

    /**
     * Set the dirty flag, and also return the timestamp of the isDirty event
     *
     * @return the timestamp when the isDirty flag is set
     */
    public synchronized long setIsDirtyWithTime() {
        setIsDirty();
        return lastDirtyTimestamp;
    }


    /**
     * Unset the dirty flag iff the unsetDirtyTimestamp matches the lastDirtyTimestamp. No-op if
     * lastDirtyTimestamp > unsetDirtyTimestamp
     *
     * @param unsetDirtyTimestamp the expected lastDirtyTimestamp to unset.
     */
    public synchronized void unsetIsDirty(long unsetDirtyTimestamp) {
        if (lastDirtyTimestamp <= unsetDirtyTimestamp) {
            isInstanceInfoDirty = false;
        } else {
        }
    }

    /**
     * Finds if this instance is the coordinating discovery server.
     *
     * @return - true, if this instance is the coordinating discovery server,
     * false otherwise.
     */
    @JsonProperty("isCoordinatingDiscoveryServer")
    public String isCoordinatingDiscoveryServer() {
        return isCoordinatingDiscoveryServer;
    }

    public InstanceInfo.ActionType getActionType() {
        return actionType;
    }

    /**
     * Set the action type performed on this instance in the server.
     *
     * @param actionType action type done on the instance.
     */
    public void setActionType(InstanceInfo.ActionType actionType) {
        this.actionType = actionType;
    }

    /**
     * Get AWS autoscaling group name if any.
     *
     * @return autoscaling group name of this instance.
     */
    @JsonProperty("asgName")
    public String getASGName() {
        return this.asgName;
    }

    public enum ActionType {
        ADDED, // Added in the discovery server
        MODIFIED, // Changed in the discovery server
        DELETED
        // Deleted from the discovery server
    }

    /**
     * Register application specific metadata to be sent to the discovery
     * server.
     *
     * @param runtimeMetadata
     *            Map containing key/value pairs.
     */
    synchronized void registerRuntimeMetadata(
            Map<String, String> runtimeMetadata) {
        metadata.putAll(runtimeMetadata);
        setIsDirty();
    }
}
