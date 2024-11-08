package pl.szymanski.user.service.mapper.helper.impl;

import io.swagger.client.model.UserRepresentation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.szymanski.user.service.constants.ApplicationConstants;
import pl.szymanski.user.service.mapper.helper.UserMapperHelper;
import pl.szymanski.user.service.model.Role;
import pl.szymanski.user.service.service.RoleService;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@Component
public class UserMapperHelperImpl implements UserMapperHelper {

	private static final Logger LOG = LoggerFactory.getLogger(UserMapperHelperImpl.class);

	@Autowired
	private RoleService roleService;

	@Override
	public String mapStringAttribute(UserRepresentation userRepresentation, String attributeName) {
		Map<String, List<String>> attributes = userRepresentation.getAttributes() != null ? userRepresentation.getAttributes() : Map.of();
		if (attributes.containsKey(attributeName)) {
			return (String) attributes.get(attributeName).get(0);
		}
		return StringUtils.EMPTY;
	}

	@Override
	public Date mapBirthDate(UserRepresentation userRepresentation) {
		String birthdate = mapStringAttribute(userRepresentation, ApplicationConstants.KeyCloak.BIRTHDATE);
		if (StringUtils.isNotEmpty(birthdate)) {
			return Date.valueOf(birthdate);
		}
		return null;
	}

	@Override
	public Role mapRole(UserRepresentation userRepresentation) {
		final List<String> groups = userRepresentation.getGroups();
		if (groups != null && groups.size() != 1) {
			LOG.warn("User has more than one role assigned. User id: {}", userRepresentation.getId());
		}
		if (groups != null && groups.size() == 1) {
			String name = groups.get(0);
			if (name.startsWith("/")) {
				name = name.substring(1);
			}
			return roleService.getByName(name);
		}
		return null;
	}
}
