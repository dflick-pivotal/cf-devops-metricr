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
public class Service {
	public Service() {
	}

	@Id
	private String guid;

	@Id
	@Temporal(TemporalType.DATE)
	private Date dateLoaded;

	private String label;
	private String provider;
	private String serviceUrl;
	@Column(length = 1024)
	private String description;
	@Column(length = 1024)
	private String longDescription;
	private String version;
	private String infoUrl;
	private Boolean active;
	private Boolean bindable;
	private String uniqueId;
	private String extra;
	private String tags;
	private String requires;
	private String documentationUrl;
	private String serviceBrokerGuid;
	private String serviceBrokerName;
	private Boolean planUpdateable;
	private Boolean bindingsRetrievable;
	private Boolean instancesRetrievable;
	private Boolean allowContextUpdates;
	private String servicePlansUrl;

	private String url;
	@Basic
	private Instant createdAt;
	@Basic
	private Instant updatedAt;
}
