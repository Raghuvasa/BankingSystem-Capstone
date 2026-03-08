package com.vgr.auth_service.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.vgr.auth_service.dto.RegisterRequest;
import com.vgr.auth_service.entity.Customer;
import com.vgr.auth_service.entity.EmailOtp;
import com.vgr.auth_service.entity.User;
import com.vgr.auth_service.exception.CustomException;
import com.vgr.auth_service.repository.OtpRepository;
import com.vgr.auth_service.repository.UserRepository;

@Service
public class AuthService {

	private static final Logger log = LoggerFactory.getLogger(UserService.class);
	
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private OtpRepository otpRepo;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private EmailService emailService;

    public User register(RegisterRequest request) {

        if (userRepo.findByUsername(request.username).isPresent()) {
            throw new CustomException("User already exists", 400);
        }

        if (userRepo.existsByEmail(request.email))
            throw new CustomException("Email already registered", 409);

        if (userRepo.existsByMobileNumber(request.mobileNumber))
            throw new CustomException("Mobile number already registered", 409);

        if (userRepo.existsByUsername(request.username))
            throw new CustomException("Username already exists", 409);

        
        User user = new User();
        user.setUsername(request.username);
        user.setPassword(passwordEncoder.encode(request.password));
        user.setUserRole(request.userRole);
        user.setEmail(request.email);
        user.setMobileNumber(request.mobileNumber);
        
        userRepo.save(user);
        
        
//        String otp = generateOtp();
//
//        EmailOtp emailOtp = new EmailOtp();
//        emailOtp.setEmail(request.email);
//        emailOtp.setOtp(otp);
//        emailOtp.setExpiryTime(LocalDateTime.now().plusMinutes(5));
//
//        otpRepo.save(emailOtp);
//
//        emailService.sendOtpEmail(request.email, otp);

        return user;
        
    }
    
    public boolean sendOtpEmail(String email) {
    	
    	String otp = generateOtp();

        EmailOtp emailOtp = new EmailOtp();
        emailOtp.setEmail(email);
        emailOtp.setOtp(otp);
        emailOtp.setExpiryTime(LocalDateTime.now().plusMinutes(5));

        otpRepo.save(emailOtp);

        if(!emailService.sendOtpEmail(email, otp)) {
        	return false;
        }else {
        	return true;
        }
        
    }
    
    public ResponseEntity<String> verifyOtp(String email, String otp) {

        EmailOtp emailOtp = otpRepo.findByEmail(email)
                .orElseThrow(() -> new CustomException("Invalid OTP", 400));

        if (emailOtp.getExpiryTime().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("OTP Expired");
        }

        if (!emailOtp.getOtp().equals(otp)) {
            return ResponseEntity.badRequest().body("Invalid OTP");
        }

        User user = userRepo.findByEmail(email);
        user.setEmailEnabled(true);
        userRepo.save(user);

        otpRepo.delete(emailOtp);

        return ResponseEntity.ok("Email verified successfully");
    }
    
    
    public String generateOtp() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }


	public User validateUser(String userName, String password) {

		User user = userRepo.findByUsername(userName)
				.orElseThrow(() -> new CustomException("Invalid credentials", 401));
		
		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new CustomException("Invalid credentials", 401);
		}
		
		if(!user.isEmailEnabled()) {
			throw new CustomException("Email not verified......", 403);
		}
		return user;
	}
}
