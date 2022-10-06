package com.kyn.api.kynapi.Security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.kyn.api.kynapi.Model.User;
import com.kyn.api.kynapi.Repository.UserRepository;

@Component
public class CustomerUserDetailsService implements UserDetailsService{

    @Autowired
    UserRepository uRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = uRepository.findByEmail(username);
        if (user == null){
            throw new UsernameNotFoundException("no email found");
        }
        return UserPrincipal.create(user);
    }

    public UserDetails loadUserById(Long id) {
        Optional<User> user = this.uRepository.findById(id);
        
        if(user.isEmpty()){
            throw new UsernameNotFoundException("email does not exist");
        }

        return UserPrincipal.create(user.get());
    }
    
}
