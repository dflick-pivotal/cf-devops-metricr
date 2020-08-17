package io.pivotal.metricr.loader.helper;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

import org.cloudfoundry.client.v2.buildpacks.BuildpackResource;
import org.cloudfoundry.client.v2.spaces.SpaceResource;
import org.cloudfoundry.client.v2.stacks.StackResource;

import io.pivotal.metricr.domain.Buildpack;

public class BuildpackHelper {

	public Buildpack toEntity(BuildpackResource cfbuildpack) {
		Buildpack buildpack = new Buildpack();

		buildpack.setDateLoaded(new Date());

		buildpack.setName(cfbuildpack.getEntity().getName());
//		buildpack.setStack(cfbuildpack.getEntity().getStack());
		buildpack.setPosition(cfbuildpack.getEntity().getPosition());
		buildpack.setEnabled(cfbuildpack.getEntity().getEnabled());
		buildpack.setLocked(cfbuildpack.getEntity().getLocked());
		buildpack.setFilename(cfbuildpack.getEntity().getFilename());

		buildpack.setCreatedAt(Instant.parse(cfbuildpack.getMetadata().getCreatedAt()));
		buildpack.setGuid(cfbuildpack.getMetadata().getId());

		String updatedAt = cfbuildpack.getMetadata().getUpdatedAt();
		if (!(updatedAt==null))
			buildpack.setUpdatedAt(Instant.parse(cfbuildpack.getMetadata().getUpdatedAt()));
		
		buildpack.setUrl(cfbuildpack.getMetadata().getUrl());

		return buildpack;
	}

}
