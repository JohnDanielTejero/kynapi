package com.kyn.api.kynapi.Security.oauth;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.kyn.api.kynapi.Exceptions.OAuth2AuthenticationProcessingException;
import com.kyn.api.kynapi.Model.User;
import com.kyn.api.kynapi.Model.ModelUtil.Provider;

import com.kyn.api.kynapi.Repository.UserRepository;
import com.kyn.api.kynapi.Security.UserPrincipal;
import com.kyn.api.kynapi.Security.oauth.user.GoogleAuth;
import com.kyn.api.kynapi.Services.RoleService;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService{

    @Autowired
    UserRepository uRepository;

    @Autowired
    RoleService rService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauthUser = super.loadUser(userRequest);
        try {
            return processOAuth2User(userRequest, oauthUser);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        if(oAuth2UserRequest.getClientRegistration().getRegistrationId().equalsIgnoreCase(Provider.GOOGLE.toString())){
            GoogleAuth googleAuth = new GoogleAuth(oAuth2User.getAttributes());
            User userOptional = uRepository.findByEmail(googleAuth.getEmail());
            User user;
            if(userOptional != null) {
                user = userOptional;
                if(!user.getProvider().equals(Provider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase()))) {
                    throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                            user.getProvider() + " account. Please use your " + user.getProvider() +
                            " account to login.");
                }
            } else {
                user = registerNewUser(oAuth2UserRequest, googleAuth);
            }
            return UserPrincipal.create(user, oAuth2User.getAttributes());
        }
        //System.out.println(oAuth2UserRequest.getClientRegistration().getRegistrationId());
        throw new OAuth2AuthenticationException("only google is supported for OAuth2");
    }

    /**
     * @param oAuth2UserRequest
     * @param oAuth2UserInfo
     * @return
     */
    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, GoogleAuth oAuth2UserInfo) {
        User user = new User();
        user.setProvider(Provider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase()));
        user.setProviderId(oAuth2UserInfo.getId());
        user.setFirstName(oAuth2UserInfo.getFirstName());
        user.setLastName(oAuth2UserInfo.getLastName());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setImgPath(oAuth2UserInfo.getImageUrl());
        user.setRegistrationDate(new Date());
        user.addRole(rService.convert("ROLE_USER"));
        return uRepository.save(user);
    }

}
