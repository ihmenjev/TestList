import java.util.UUID;


public class Person {
private String mName;
private String mSurname;
private String mLogin;
private String mEmail;
private String mPhoneN;
private UUID mId;
private int iId;
public Person(String name, String surname, String login, String email,
		String phoneN) {
	
	mName = name;
	mSurname = surname;
	mLogin = login;
	mEmail = email;
	mPhoneN = phoneN;
	mId = UUID.randomUUID();
}
public Person(int id, String name, String surname, String login, String email,
		String phoneN) {
	this.iId = id; 
	mName = name;
	mSurname = surname;
	mLogin = login;
	mEmail = email;
	mPhoneN = phoneN;
	mId = UUID.randomUUID();
}


public int getiId() {
	return iId;
}
public UUID getId() {
	return mId;
}

public String getName() {
	return mName;
}
public void setName(String name) {
	mName = name;
}
public String getSurname() {
	return mSurname;
}
public void setSurname(String surname) {
	mSurname = surname;
}
public String getLogin() {
	return mLogin;
}
public void setLogin(String login) {
	mLogin = login;
}
public String getEmail() {
	return mEmail;
}
public void setEmail(String email) {
	mEmail = email;
}
public String getPhoneN() {
	return mPhoneN;
}
public void setPhoneN(String phoneN) {
	mPhoneN = phoneN;
}



}
