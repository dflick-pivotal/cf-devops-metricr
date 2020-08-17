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
public class Organization {

	public Organization() {
	}

	@Id
	private String guid;

	@Id
	@Temporal(TemporalType.DATE)
	private Date dateLoaded;

	private String name;
	private Boolean billingEnabled;
	private String quotaDefinitionGuid;
	private String status;
	private String defaultIsolationSegmentGuid;
	private String quotaDefinitionUrl;
	private String spacesUrl;
	private String domainsUrl;
	private String privateDomainsUrl;
	private String usersUrl;
	private String managersUrl;
	private String billingManagersUrl;
	private String auditorsUrl;
	private String appEventsUrl;
	private String spaceQuotaDefinitionsUrl;

	private String url;
	@Basic
	private Instant createdAt;
	@Basic
	private Instant updatedAt;
}
