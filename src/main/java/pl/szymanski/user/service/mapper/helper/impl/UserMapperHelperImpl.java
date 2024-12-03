package pl.szymanski.user.service.mapper.helper.impl;

import io.swagger.client.model.CredentialRepresentation;
import io.swagger.client.model.UserRepresentation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.szymanski.springfrontend.avro.UpdateUserEvent;
import pl.szymanski.user.service.constants.ApplicationConstants;
import pl.szymanski.user.service.dto.AddUserDTO;
import pl.szymanski.user.service.mapper.helper.UserMapperHelper;
import pl.szymanski.user.service.model.Role;
import pl.szymanski.user.service.model.User;
import pl.szymanski.user.service.service.RoleService;

import java.sql.Date;
import java.util.HashMap;
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

	@Override
	public Map<String, List<String>> mapAttributes(final User user) {
		final Map<String, List<String>> attributes = new HashMap<>();
		attributes.put(ApplicationConstants.KeyCloak.PHONE, List.of(user.getPhone()));
		attributes.put(ApplicationConstants.KeyCloak.ADDRESS_LINE_1, List.of(user.getAddressLine1()));
		attributes.put(ApplicationConstants.KeyCloak.TOWN, List.of(user.getTown()));
		attributes.put(ApplicationConstants.KeyCloak.POSTAL_CODE, List.of(user.getPostalCode()));
		attributes.put(ApplicationConstants.KeyCloak.BIRTHDATE, List.of(user.getDayOfBirth().toString()));
		return attributes;
	}

	@Override
	public List<String> mapRoles(final User user) {
		if (user.getRole() != null) {
			return createGroupList(user.getRole());
		}
		return List.of();
	}

	@Override
	public Role mapRole(UpdateUserEvent event) {
		return roleService.getById(event.getRoleId());
	}

	@Override
	public List<String> mapGroups(String roleId) {
		Role roleByName = roleService.getById(roleId);
		if (roleByName != null) {
			return createGroupList(roleByName);
		}
		return List.of();
	}

	@Override
	public Map<String, List<String>> mapAttributes(AddUserDTO user) {
		final Map<String, List<String>> attributes = new HashMap<>();
		attributes.put(ApplicationConstants.KeyCloak.PHONE, List.of(user.getPhone()));
		attributes.put(ApplicationConstants.KeyCloak.ADDRESS_LINE_1, List.of(user.getAddressLine1()));
		attributes.put(ApplicationConstants.KeyCloak.TOWN, List.of(user.getTown()));
		attributes.put(ApplicationConstants.KeyCloak.POSTAL_CODE, List.of(user.getPostalCode()));
		attributes.put(ApplicationConstants.KeyCloak.BIRTHDATE, List.of(user.getDayOfBirth()));
		return attributes;
	}

	@Override
	public List<CredentialRepresentation> mapCredentials(String password) {
		final CredentialRepresentation credential = new CredentialRepresentation();
		credential.setType(ApplicationConstants.KeyCloak.CREDENTIAL_TYPE_PASSWORD);
		credential.setValue(password);
		return List.of(credential);
	}

	private List<String> createGroupList(Role role) {
		return List.of("/" + role.getName());
	}
}
