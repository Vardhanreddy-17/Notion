package com.v1.Notion.DTO;

public class UserResponseDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String accountType;
    private boolean approved;
    private String image;
    public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String token;

    public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public UserResponseDTO(String firstName, String lastName, String email, 
                           String accountType, boolean approved, String image,String token) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.accountType = accountType;
        this.approved = approved;
        this.image = image;
        this.token = token;
    }


}
