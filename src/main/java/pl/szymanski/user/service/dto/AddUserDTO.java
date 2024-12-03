package pl.szymanski.user.service.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.szymanski.user.service.constants.ApplicationConstants;
import pl.szymanski.user.service.validation.ValidRole;

@NoArgsConstructor
@Setter
@Getter
public class AddUserDTO {


	@NotEmpty(message = ApplicationConstants.ValidationMessages.EMAIL_EMPTY)
	@Email(message = ApplicationConstants.ValidationMessages.EMAIL_INVALID)
	private String email;
	@NotEmpty(message = ApplicationConstants.ValidationMessages.NAME_EMPTY)
	private String name;
	@NotEmpty(message = ApplicationConstants.ValidationMessages.LAST_NAME_EMPTY)
	private String lastName;
	@NotEmpty(message = ApplicationConstants.ValidationMessages.DAY_OF_BIRTH_EMPTY)
	@Pattern(regexp = "\\d\\d\\d\\d-\\d\\d-\\d\\d", message = ApplicationConstants.ValidationMessages.DAY_OF_BIRTH_INVALID)
	private String dayOfBirth;
	@NotEmpty(message = ApplicationConstants.ValidationMessages.PHONE_EMPTY)
	@Digits(integer = 9, fraction = 0, message = ApplicationConstants.ValidationMessages.PHONE_INVALID)
	private String phone;
	@NotEmpty(message = ApplicationConstants.ValidationMessages.ADDRESS_LINE_1_EMPTY)
	private String addressLine1;

	@NotEmpty(message = ApplicationConstants.ValidationMessages.TOWN_EMPTY)
	private String town;

	@NotEmpty(message = ApplicationConstants.ValidationMessages.POSTAL_CODE_EMPTY)
	private String postalCode;
	@NotEmpty(message = ApplicationConstants.ValidationMessages.ROLE_EMPTY)
	@ValidRole
	private String roleId;

	@NotEmpty(message = ApplicationConstants.ValidationMessages.PASSWORD_EMPTY)
	private String password;
}
