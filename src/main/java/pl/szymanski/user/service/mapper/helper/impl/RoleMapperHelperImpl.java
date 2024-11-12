package pl.szymanski.user.service.mapper.helper.impl;

import io.swagger.client.model.GroupRepresentation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import pl.szymanski.user.service.mapper.helper.RoleMapperHelper;

import java.util.List;
import java.util.Map;

@Component
public class RoleMapperHelperImpl implements RoleMapperHelper {

	@Override
	public String mapStringAttribute(GroupRepresentation userRepresentation, String attributeName) {
		final Map<String, List<String>> attributes = userRepresentation.getAttributes() != null ? userRepresentation.getAttributes() : Map.of();
		if (attributes.containsKey(attributeName)) {
			return (String) attributes.get(attributeName).get(0);
		}
		return StringUtils.EMPTY;
	}
}
