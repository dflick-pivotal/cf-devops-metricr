package io.pivotal.metricr.loader.helper;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import org.cloudfoundry.client.v2.events.EventResource;

import io.pivotal.metricr.domain.Event;
import io.pivotal.metricr.domain.Organization;
import io.pivotal.metricr.domain.Space;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class EventHelper {

	public Event toEntity(EventResource cfEvent, ArrayList<Organization> organizationsList,
			ArrayList<Space> spacesList) {
		Event event = new Event();
		
		Map<String, Space> spaces = spacesList.stream()
			      .collect(Collectors.toMap(Space::getGuid, space -> space));

		Map<String, Organization> organizations = organizationsList.stream()
			      .collect(Collectors.toMap(Organization::getGuid, organization -> organization));


//		log.info(cfEvent);
		event.setActee(cfEvent.getEntity().getActee());
		event.setActeeName(cfEvent.getEntity().getActeeName());
		event.setActeeType(cfEvent.getEntity().getActeeType());
		event.setActor(cfEvent.getEntity().getActor());
		event.setActorName(cfEvent.getEntity().getActorName());
		event.setActorType(cfEvent.getEntity().getActorType());
		event.setActorUsername(cfEvent.getEntity().getActorUserName());

		String organizationId = cfEvent.getEntity().getOrganizationId();
		event.setOrganizationGuid(organizationId);
		if (!organizationId.isEmpty()) {
			Organization o = organizations.get(organizationId);
			if (o != null)
				event.setOrganizationName(o.getName());
		}

		String spaceId = cfEvent.getEntity().getSpaceId();
		event.setSpaceGuid(spaceId);
		if (!spaceId.isEmpty()) {
			Space s = spaces.get(spaceId);
			if (s != null)
				event.setSpaceName(s.getName());
		}

		event.setTimestamp(Instant.parse(cfEvent.getEntity().getTimestamp()));
		event.setType(cfEvent.getEntity().getType());
		String m = cfEvent.getEntity().getMetadatas().toString();
		event.setMetadatas(m.length() > 1024 ? m.substring(0, 1023) : m);

		event.setCreatedAt(Instant.parse(cfEvent.getMetadata().getCreatedAt()));
		event.setGuid(cfEvent.getMetadata().getId());

		String updatedAt = cfEvent.getMetadata().getUpdatedAt();
		if (!(updatedAt == null))
			event.setUpdatedAt(Instant.parse(cfEvent.getMetadata().getUpdatedAt()));

		event.setUrl(cfEvent.getMetadata().getUrl());
//		log.info(event);
		return event;
	}
}
