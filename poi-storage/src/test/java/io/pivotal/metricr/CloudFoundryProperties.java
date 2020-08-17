package io.pivotal.metricr;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CloudFoundryProperties {

    /**
     * name of instance
     */
    private String name;

    /**
     * URL of Cloud Foundry API (Cloud Controller).
     */
    private String url;

    /**
     * Username to authenticate (usually an email address).
     */
    private String username;

    /**
     * Password for user to authenticate and obtain token.
     */
    private String password;

    /**
     * Organization name to initially target.
     */
    private String org;

    /**
     * Space name to initially target.
     */
    private String space;

    private boolean skipSslValidation;

    private Integer connectionPoolSize;

    private String proxyHost;
    private String proxyPassword;
    private Integer proxyPort;
    private String proxyUsername;
    private String proxyType;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String cloudControllerUrl) {
        this.url = safeUrl(cloudControllerUrl);
    }

    private String safeUrl(String t) {
        String input = t.trim().toLowerCase();
        Pattern p = Pattern.compile("(http(s)?://)(.*)");
        Matcher matcher = p.matcher(input);
        if (matcher.matches()) {
            String group = matcher.group(1);
            if (group != null && !group.isEmpty() && group.trim().isEmpty()) {
                return t.substring(group.length());
            }
        }
        return t;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String email) {
        this.username = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOrg() {
        return this.org;
    }

    public void setOrg(String org) {
        this.org = org;
        if (this.org != null) {
            this.org = this.org.trim();
        }
    }

    public String getSpace() {
        return this.space;
    }

    public void setSpace(String space) {
        this.space = space;
        if (this.space != null) {
            this.space = this.space.trim();
        }
    }

    public boolean isSkipSslValidation() {
        return this.skipSslValidation;
    }

    public boolean getSkipSslValidation() {
        return this.skipSslValidation;
    }

    public void setSkipSslValidation(boolean skipSslValidation) {
        this.skipSslValidation = skipSslValidation;
    }

    public Integer getConnectionPoolSize() {
        return connectionPoolSize;
    }

    public void setConnectionPoolSize(Integer connectionPoolSize) {
        this.connectionPoolSize = connectionPoolSize;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    public Integer getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(Integer proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxyUsername() {
        return proxyUsername;
    }

    public void setProxyUsername(String proxyUsername) {
        this.proxyUsername = proxyUsername;
    }

    public String getProxyType() {
        return proxyType;
    }

    public void setProxyType(String proxyType) {
        this.proxyType = proxyType;
    }

    @Override
    public String toString() {
        return "CloudFoundryProperties{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", org='" + org + '\'' +
                ", space='" + space + '\'' +
                ", skipSslValidation=" + skipSslValidation +
                ", connectionPoolSize=" + connectionPoolSize +
                ", proxyHost='" + proxyHost + '\'' +
                ", proxyPassword='" + proxyPassword + '\'' +
                ", proxyPort=" + proxyPort +
                ", proxyUsername='" + proxyUsername + '\'' +
                ", proxyType='" + proxyType + '\'' +
                '}';
    }
}
