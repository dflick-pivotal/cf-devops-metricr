package io.pivotal.metricr.domain;

import java.time.Instant;
import java.util.Date;

import javax.persistence.Basic;
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
public class Space {
	
	public Space() {
	}

	@Id
	private String guid;

	@Id
	@Temporal(TemporalType.DATE)
	private Date dateLoaded;

	private String name;
	private String organizationGuid;
	private String spaceQuotaDefinitionGuid;
	private String isolationSegmentGuid;
	private Boolean allowSsh;
	private String organizationUrl;
	private String developersUrl;
	private String managersUrl;
	private String auditorsUrl;
	private String appsUrl;
	private String routesUrl;
	private String domainsUrl;
	private String serviceInstancesUrl;
	private String appEventsUrl;
	private String eventsUrl;
	private String securityGroupsUrl;
	private String stagingSecurityGroupsUrl;

	private String url;
	@Basic
	private Instant createdAt;
	@Basic
	private Instant updatedAt;
}
