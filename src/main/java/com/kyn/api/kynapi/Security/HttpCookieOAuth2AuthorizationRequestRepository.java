package com.kyn.api.kynapi.Security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import com.nimbusds.oauth2.sdk.util.StringUtils;

@Component
public class HttpCookieOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest>{

    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    private static final int cookieExpireSeconds = 180;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return CookieGenerator.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
        .map(cookie -> CookieGenerator.deserialize(cookie, OAuth2AuthorizationRequest.class))
        .orElse(null);
    }
    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request,
            HttpServletResponse response) {
                if (authorizationRequest == null) {
                    CookieGenerator.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
                    CookieGenerator.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
                    return;
                }

                CookieGenerator.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, CookieGenerator.serialize(authorizationRequest), cookieExpireSeconds);
                String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
                if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
                    CookieGenerator.addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin, cookieExpireSeconds);
                }
        
    }
    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
        return this.loadAuthorizationRequest(request);
    }

    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieGenerator.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        CookieGenerator.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
    }

}
