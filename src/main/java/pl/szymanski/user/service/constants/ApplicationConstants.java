package pl.szymanski.user.service.constants;

public class ApplicationConstants {

	public static final String USER_ROLE_NAME = "ROLE_USER";
	public static final String ROLE_EMPLOYEE_NAME = "ROLE_EMPLOYEE";


	public class KeyCloak {
		public static final String EMAIL = "email";
		public static final String FIRST_NAME = "firstName";
		public static final String LAST_NAME = "lastName";
		public static final String ADDRESS_LINE_1 = "addressLine1";
		public static final String PHONE = "phone";
		public static final String BIRTHDATE = "dayOfBirth";
		public static final String TOWN = "town";
		public static final String POSTAL_CODE = "postalCode";
		public static final String DESCRIPTION = "description";
		public static final String UUID = "id";
		public static final String RESOURCE_TYPE_USER = "USER";
		public static final String RESOURCE_TYPE_GROUP_MEMBERSHIP = "GROUP_MEMBERSHIP";
		public static final String EVENT_TYPE_CREATE = "CREATE";
		public static final String EVENT_TYPE_UPDATE = "UPDATE";
		public static final String EVENT_TYPE_DELETE = "DELETE";
		public static final String CREDENTIAL_TYPE_PASSWORD = "password";
	}

	public class ValidationMessages {
		public static final String EMAIL_EMPTY = "Email cannot be empty";
		public static final String NAME_EMPTY = "Name cannot be empty";
		public static final String LAST_NAME_EMPTY = "Last name cannot be empty";
		public static final String DAY_OF_BIRTH_EMPTY = "Day of birth cannot be empty";
		public static final String PHONE_EMPTY = "Phone cannot be empty";
		public static final String PHONE_INVALID = "Phone number is invalid";
		public static final String ADDRESS_LINE_1_EMPTY = "Address line 1 cannot be empty";
		public static final String TOWN_EMPTY = "Town cannot be empty";
		public static final String POSTAL_CODE_EMPTY = "Postal code cannot be empty";
		public static final String INVALID_ROLE = "Invalid role";
		public static final String PASSWORD_EMPTY = "Password cannot be empty";
		public static final String ROLE_EMPTY = "Role cannot be empty";
		public static final String EMAIL_INVALID = "Email is invalid";
		public static final String DAY_OF_BIRTH_INVALID = "Day of birth is invalid";
	}
}
