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
public class ServiceInstance {
	
	public ServiceInstance() {
	}

	@Id
	private String guid;

	@Id
	@Temporal(TemporalType.DATE)
	private Date dateLoaded;

	private String name;
	private String credentials;
	private String servicePlanGuid;
	private String spaceGuid;
	private String gatewayData;
	private String dashboardUrl;
	private String type;
	@Column(length = 1024)
	private String lastOperation;
	private String tags;
	private String maintenanceInfo;
	private String serviceGuid;
	private String spaceUrl;
	private String servicePlanUrl;
	private String serviceBindingsUrl;
	private String serviceKeysUrl;
	private String routesUrl;
	private String serviceUrl;
	private String sharedFromUrl;
	private String sharedToUrl;
	private String serviceInstanceParametersUrl;

	private String url;
	@Basic
	private Instant createdAt;
	@Basic
	private Instant updatedAt;
}
