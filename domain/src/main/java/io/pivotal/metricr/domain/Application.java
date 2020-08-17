package io.pivotal.metricr.domain;

import java.time.Instant;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
@IdClass(CompositeKey.class)
public class Application {

	public Application() {
	}

	@Id
	private String guid;

	@Id
	@Temporal(TemporalType.DATE)
	private Date dateLoaded;
	
	private String space;
	private String stack;

	private String name;
	private Boolean production;
	private String spaceGuid;
	private String stackGuid;
	private String buildpack;
	private String detectedBuildpack;
	private String detectedBuildpackGuid;
	@Column(length = 1024)
	private String environment;
	private Integer memory;
	private Integer instances;
	private Integer diskQuota;
	private String state;
	private String version;
	@Column(length = 2048)
	private String command;
	private Boolean console;
	private String debug;
	private String stagingTaskId;
	private String packageState;
	private String healthCheckType;
	private Integer healthCheckTimeout;
	private String healthCheckHttpEndpoint;
	private String stagingFailedReason;
	private String stagingFailedDescription;
	private Boolean diego;
	private String dockerImage;
	private String dockerCredentials;
	private String packageUpdatedAt;
	@Column(length = 2048)
	private String detectedStartCommand;
	private Boolean enableSsh;
	private String ports;
	private String spaceUrl;
	private String stackUrl;
	private String routesUrl;
	private String eventsUrl;
	private String serviceBindingsUrl;
	private String routeMappingsUrl;

	private String url;
	@Basic
	private Instant createdAt;
	@Basic
	private Instant updatedAt;

}
