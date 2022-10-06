package com.kyn.api.kynapi.Controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kyn.api.kynapi.Model.User;
import com.kyn.api.kynapi.Model.ModelUtil.Provider;
import com.kyn.api.kynapi.Model.Payload.AuthProvider;
import com.kyn.api.kynapi.Security.TokenProvider;
import com.kyn.api.kynapi.Services.RoleService;
import com.kyn.api.kynapi.Services.UserService;

@RestController
@RequestMapping("/kynapi/open")
@CrossOrigin(origins = "http://localhost:3000")
@PreAuthorize("permitAll()")
public class UnauthApiController {

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    RoleService roleService;

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider tokenProvider;

    @PostMapping("/register")
    public ResponseEntity<?> Register(@RequestBody User user){
        user.setRegistrationDate(new Date());
        user.setProvider(Provider.LOCAL);
        user.addRole(roleService.convert("ROLE_USER"));
        user.setPassword(encoder.encode(user.getPassword()));
        return new ResponseEntity<>(userService.updateAndInsertUser(user), HttpStatus.OK);
    }

    @GetMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable("id") int id){
        userService.deleteUser(userService.findById(id));
        return "deleted";
    }

    @GetMapping("email-exist/{email}")
    public ResponseEntity<?> findEmail(@PathVariable("email")String email){
        if (userService.findByEmail(email) != null){
            return new ResponseEntity<>("Email already exists", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Email is available", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User payload){
        try {
            Authentication auth = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(payload.getEmail(), payload.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(auth);

            //String token = tokenProvider.createToken(auth);
            return new ResponseEntity<>(new AuthProvider(tokenProvider.createToken(auth)), HttpStatus.OK);

        } catch (BadCredentialsException ex) {
            return new ResponseEntity<>(ex, HttpStatus.UNAUTHORIZED);
        }
    }
}