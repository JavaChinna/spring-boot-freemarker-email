package com.javachinna.model;

public class User {

	/**
	 * @param id
	 * @param displayName
	 * @param email
	 */
	public User(Long id, String displayName, String email) {
		this.id = id;
		this.displayName = displayName;
		this.email = email;
	}

	private Long id;
	private String displayName;
	private String email;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
