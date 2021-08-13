package com.atan.test.jwt.controller;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atan.test.jwt.dao.UserDao;
import com.atan.test.jwt.dto.Response;
import com.atan.test.jwt.dto.ResponseCode;
import com.atan.test.jwt.entity.User;
import com.atan.test.jwt.service.MyService;
import com.atan.test.jwt.util.JwtTokenUtil;

import io.jsonwebtoken.ExpiredJwtException;


@Controller
public class RestController {
	private Logger logger = Logger.getLogger(RestController.class.getName());

	
	@Autowired
	private UserDao userDao;

	@Autowired
	private MyService myService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@PostMapping("/api/login")
	@ResponseBody
	public Response<User> login(
			@RequestParam(value = "username", required = true) final String username,
			@RequestParam(value = "password", required = true) final String password
			) {
		Response<User> resp =  new Response<User>(ResponseCode.FAILED);

		User user = userDao.findByUsernameAndPassword(username, password);
		if(user != null){
			final String token = jwtTokenUtil.generateToken(user);
			user.setToken(token);
			resp = new Response<User>(ResponseCode.SUCCESS,user);
		}
		return resp; 
	}

	@GetMapping(value="/api/job")
	@ResponseBody
	public Response getPosition(@RequestHeader("Authorization") String authorization) {
		Response resp = new Response(ResponseCode.FAILED);
		
		logger.info("authorization: "+ authorization);
		String errorMsg = authorizationValidator(authorization);
		if(!errorMsg.isEmpty()) {
			resp.setMessage(errorMsg);
		}else {
			resp = new Response(ResponseCode.SUCCESS, myService.positionRestCall().block());	
		}
		return resp;
		
	}

	@GetMapping(value="/api/job/{id}")
	@ResponseBody
	public Response getPositionById(@PathVariable String id, @RequestHeader("Authorization") String authorization) {
		Response resp = new Response(ResponseCode.FAILED);
		String errorMsg = authorizationValidator(authorization);
		if(!errorMsg.isEmpty()) {
			resp.setMessage(errorMsg);
		}else if(id != null){
			resp = new Response(ResponseCode.SUCCESS, myService.detailRestCall(id).block());
		}
		return resp;
		
	}

	private String authorizationValidator(String requestTokenHeader) {
		String errorMsg = "";
		String username = null;
		String jwtToken = null;
		// JWT Token is in the form "Bearer token". Remove Bearer word and get
		// only the Token
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				errorMsg = "Unable to get JWT Token";
			} catch (ExpiredJwtException e) {
				errorMsg = "JWT Token has expired";
			}
		} else {
			errorMsg = "JWT Token does not begin with Bearer String";
		}

		// Once we get the token validate it.
		if (username != null) {
			User user = userDao.findByUsername(username);			
			// if token is valid configure Spring Security to manually set
			// authentication
			if (!jwtTokenUtil.validateToken(jwtToken, user)) {
				errorMsg = "Authorization is invalid";
			}
		}else {
			errorMsg = "Username is null";
		}
		return errorMsg;
	}
	/*
	 * @RequestMapping(value="/registration", method = RequestMethod.GET)
	 * 
	 * @ResponseBody public Page<User> findAllUser(
	 * 
	 * @PageableDefault(size = 10) Pageable pageable) {
	 * 
	 * Page<User> registrations = userDao.findAll(pageable);
	 * 
	 * return registrations; }
	 * 
	 * @RequestMapping(value="/registration/check-email", method =
	 * RequestMethod.GET)
	 * 
	 * @ResponseBody public Response checkEmailUser(
	 * 
	 * @RequestParam(value = "email", required = true) final String email) {
	 * 
	 * logger.info("email:"+email); Response resp = new
	 * Response(ResponseCode.FAILED); if(email != null) { User user =
	 * userDao.findByEmail(email); if(user == null) { resp = new
	 * Response(ResponseCode.SUCCESS); } }
	 * 
	 * return resp; }
	 * 
	 * 
	 * @RequestMapping(value="/registration/check-number", method =
	 * RequestMethod.GET)
	 * 
	 * @ResponseBody public Response checkMobileNumberUser(
	 * 
	 * @RequestParam(value = "mobileNumber", required = true) String mobileNumber) {
	 * mobileNumber = "+"+mobileNumber.trim();
	 * logger.info("mobileNumber:"+mobileNumber); Response resp = new
	 * Response(ResponseCode.FAILED); if(mobileNumber != null) { User user =
	 * userDao.findByMobileNumber(mobileNumber); if(user == null) { resp = new
	 * Response(ResponseCode.SUCCESS); } }
	 * 
	 * return resp; }
	 * 
	 * private Date getDate(String date){ Date dt = null; try{ if(date != null){
	 * SimpleDateFormat df2 = new SimpleDateFormat("dd-MMM-yyyy"); dt =
	 * df2.parse(date); } }catch(Exception e){ logger.info("ERROR on [getDate]"); }
	 * return dt; }
	 */

	
}
