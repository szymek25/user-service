package pl.szymanski.user.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@NoArgsConstructor
@Setter
@Getter
public class AddUserDTO {


	private String email;

	private String name;

	private String lastName;

	private String dayOfBirth;

	private String phone;

	private String addressLine1;

	private String town;

	private String postalCode;

	private String roleId;

	private String password;
}
