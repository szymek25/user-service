package pl.szymanski.user.service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

	@Id
	@Column(unique = true)
	private String id;

	@Column
	private String email;

	@Column
	private String name;

	@Column
	private String lastName;

	@Column
	private Date dayOfBirth;

	@Column
	private String phone;

	@Column
	private String addressLine1;

	@Column
	private String town;

	@Column
	private String postalCode;


}
