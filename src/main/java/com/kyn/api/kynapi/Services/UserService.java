package com.kyn.api.kynapi.Services;

import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kyn.api.kynapi.Model.User;
import com.kyn.api.kynapi.Repository.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository userrepo;

    @Transactional
    public User updateAndInsertUser(User user){
        return userrepo.save(user);
    }

	public String deleteUser(User user){
		userrepo.delete(user);
		return "deleted";
	}

	public User findByEmail(String user){
		return userrepo.findByEmail(user);
	}

    public User findById(long id){
		//System.out.println(userrepo.findById(id).get().getEmail());
        return userrepo.findByUserId(id);
    }

    
	public Boolean validateName(String name) {
		String regexp ="[a-zA-Z][a-zA-Z ]*"; /*Regular Expression pattern to be tested*/
		return Pattern.matches(regexp, name); /*Returns a true or false if Regex is satisfied*/
	}

	/**
	 * Method for email validation
	 * @param String email parameter will be used to pass email entry needed to be validated
	 * @return boolean to indicate if constraints are satisfied
	 */
	public Boolean validateEmail(String email) { 
		String regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"; /*Regular Expression pattern to be tested*/
		return Pattern.matches(regexp, email); /*Returns a true or false if Regex is satisfied*/
	}
	
	/**
	 * Method to validate contact number entry
	 * @param String contact parameter will be used to pass contact number needed to be validated
	 * @return boolean to indicate if constraints are satisfied
	 */
	public Boolean validateContact(String contact) {
		String allCountryRegex = "^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$"; /*Regular Expression pattern to be tested*/
		return Pattern.matches(allCountryRegex, contact); /*Returns a true or false if Regex is satisfied*/
	}

}
