package edu.csupomona.cs480.controller;


import java.io.IOException;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.google.common.base.Splitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import edu.csupomona.cs480.App;
import edu.csupomona.cs480.data.User;
import edu.csupomona.cs480.data.provider.UserManager;
import edu.csupomona.cs480.DAL.DataAccess;
import edu.csupomona.cs480.constants.*;

@RestController
public class UserController<T> {
	public Gson gson = new GsonBuilder().create();
	
	@Autowired
	private DataAccess userManager;
	
	@RequestMapping(value = "/user/{userId}",method = RequestMethod.GET,produces = "application/json")
	ResponseEntity<?> getUser(@PathVariable("userId") String userId) {
		try {
			User user = userManager.getUser(userId);
			
			if(user.getId() == null) {
				user.setId(UUID.randomUUID().toString());
				user.setUserName(userId);
				user.setName(userId);
				userManager.createUser(user);
			}
			return new ResponseEntity<>(gson.toJson(user),HttpStatus.OK);
		}
		catch(Exception e) {
			// !!!!! Set up error handler here!!!!
			return new ResponseEntity<>("!!!!add json string here!!!!!",HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@RequestMapping(value = "/user/", method = RequestMethod.POST, produces = "application/json")
	ResponseEntity<?> createUser(@RequestBody User user){
		try {
			// add post code
			ArrayList<ErrorPackage> errors = validateUser(user);
			if(!errors.isEmpty()) {
				return new ResponseEntity<>(gson.toJson(errors),HttpStatus.BAD_REQUEST);
			}
			// database service call here
			user.setId(UUID.randomUUID().toString());
			user = userManager.createUser(user);
			// failed creation
			if(user == null) {
				return new ResponseEntity<>(gson.toJson(new ErrorPackage(HttpStatus.BAD_REQUEST.value(), Constants.FailedCreateUser)),HttpStatus.CREATED);
			}
				
			return new ResponseEntity<>(gson.toJson(user),HttpStatus.CREATED);
		}
		catch(Exception e) {
			// !!!!! Set up error handler here!!!!
			return new ResponseEntity<>("!!!!add json string here!!!!!",HttpStatus.INTERNAL_SERVER_ERROR);		
		}
	}
	
	@RequestMapping(value = "/user/{userId}", method = RequestMethod.PUT, produces = "application/json")
	ResponseEntity<?> editUser(@PathVariable("userId") String userId, @RequestBody User user){
		try {
			// add put code
			ArrayList<ErrorPackage> errors = validateUser(user);
			if(!errors.isEmpty()) {
				return new ResponseEntity<>(gson.toJson(errors),HttpStatus.BAD_REQUEST);
			}
			// database service call here
			return new ResponseEntity<>(gson.toJson(user),HttpStatus.OK);
		}
		catch(Exception e) {
			// !!!!! Set up error handler here!!!!
			return new ResponseEntity<>("!!!!add json string here!!!!!",HttpStatus.INTERNAL_SERVER_ERROR);		
		}
	}
	
	@RequestMapping(value = "/user/{userId}", method = RequestMethod.DELETE, produces = "application/json")
	ResponseEntity<?> deleteUser(@PathVariable("userId") String userId){
		try {
			// add delete code
			if(userId == null || userId == "") {
				return new ResponseEntity<>(gson.toJson(new ErrorPackage(HttpStatus.BAD_REQUEST.value(),Constants.UserIdInvalid)),HttpStatus.BAD_REQUEST);
			}
			// call delete sp
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e) {
			// !!!!! Set up error handler here!!!!
			return new ResponseEntity<>("!!!!add json string here!!!!!",HttpStatus.INTERNAL_SERVER_ERROR);		
		}
	}
	
	private ArrayList<ErrorPackage> validateUser(User user){
		ArrayList<ErrorPackage> errors = new ArrayList<ErrorPackage>();
		ErrorPackage error;
		if(user == null) {
			error = new ErrorPackage(HttpStatus.BAD_REQUEST.value(),Constants.UserInvalid);
			errors.add(error);
			return errors;
		}
		// add more error checks
		return errors;
	} 
}
