package com.atan.test.jwt.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.atan.test.jwt.dao.UserDao;
import com.atan.test.jwt.entity.User;

@Component
public class DataLoader implements ApplicationRunner {

    private UserDao userDao;

    @Autowired
    public DataLoader(UserDao userDao) {
        this.userDao = userDao;
    }

    public void run(ApplicationArguments args) {
    	String email = "test@gmail.com";
    	User user = userDao.findByEmail(email);
    	if(user == null) {
    		user = new User();
	        user.setUsername("user");
	        user.setPassword("password");
	        user.setEmail("test@gmail.com");
	    	userDao.save(user);
    	}
    }
}