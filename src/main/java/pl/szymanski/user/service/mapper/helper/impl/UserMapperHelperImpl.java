package pl.szymanski.user.service.mapper.helper.impl;

import io.swagger.client.model.UserRepresentation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import pl.szymanski.user.service.constants.ApplicationConstants;
import pl.szymanski.user.service.mapper.helper.UserMapperHelper;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@Component
public class UserMapperHelperImpl implements UserMapperHelper {
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
}
