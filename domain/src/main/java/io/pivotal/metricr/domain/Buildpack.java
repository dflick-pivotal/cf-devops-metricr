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
public class Buildpack {

	public Buildpack() {
	}

	@Id
	private String guid;

	@Id
	@Temporal(TemporalType.DATE)
	private Date dateLoaded;

	private String name;
	private String stack;
	private Integer position;
	private Boolean enabled;
	private Boolean locked;
	private String filename;

	private String url;
	@Basic
	private Instant createdAt;
	@Basic
	private Instant updatedAt;
}
