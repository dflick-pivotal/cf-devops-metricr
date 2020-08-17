package io.pivotal.metricr.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
public class CompositeKey implements Serializable {
	private String guid;
	private Date dateLoaded;

	public CompositeKey() {}
	public CompositeKey(String guid, Date dateLoaded) {
		super();
		this.guid = guid;
		this.dateLoaded = dateLoaded;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateLoaded == null) ? 0 : dateLoaded.hashCode());
		result = prime * result + ((guid == null) ? 0 : guid.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompositeKey other = (CompositeKey) obj;
		if (dateLoaded == null) {
			if (other.dateLoaded != null)
				return false;
		} else if (!dateLoaded.equals(other.dateLoaded))
			return false;
		if (guid == null) {
			if (other.guid != null)
				return false;
		} else if (!guid.equals(other.guid))
			return false;
		return true;
	}
}
