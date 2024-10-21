package pl.szymanski.user.service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column
	private String keycloakId;

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

	@ManyToOne
	private Role role;

}
