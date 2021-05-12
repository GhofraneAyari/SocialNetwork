package models;
import java.util.*;
import java.sql.*;
import java.time.*;

public class Member {
	private
	int member_id;
	String firstName;
	String lastName;
	LocalDate birthday;
	String gender;
	String country;
	String city;
	String login;
	String password;
	
	public
	Member()
	{
		member_id=0;
		firstName="";
		lastName="";
		birthday= null;
		gender="";
		country="";
		city="";
		login="";
		password="";
	}
	
	public int getMember_id() {
		return member_id;
	}

	public void setMember_id(int member_id) {
		this.member_id = member_id;
	}

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

	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "ID=" + member_id +
				", First name='" + firstName + '\'' +
				", Last name='" + lastName + '\''
				;
	}
}
