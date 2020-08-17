package io.pivotal.metricr.loader.helper;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

import org.cloudfoundry.client.v2.spaces.SpaceResource;
import org.cloudfoundry.client.v2.stacks.StackResource;

import io.pivotal.metricr.domain.Stack;

public class StackHelper {

	public Stack toEntity(StackResource cfStack) {
		Stack stack = new Stack();

		stack.setDateLoaded(new Date());

		stack.setName(cfStack.getEntity().getName());
		stack.setDescription(cfStack.getEntity().getDescription());
		
		stack.setCreatedAt(Instant.parse(cfStack.getMetadata().getCreatedAt()));
		stack.setGuid(cfStack.getMetadata().getId());

		String updatedAt = cfStack.getMetadata().getUpdatedAt();
		if (!(updatedAt==null))
			stack.setUpdatedAt(Instant.parse(cfStack.getMetadata().getUpdatedAt()));
		
		stack.setUrl(cfStack.getMetadata().getUrl());

		return stack;
	}

}
