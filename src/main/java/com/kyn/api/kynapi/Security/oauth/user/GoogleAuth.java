package com.kyn.api.kynapi.Security.oauth.user;

import java.util.Map;

public class GoogleAuth {
    protected Map<String, Object> attributes;

    public GoogleAuth(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
    public String getId(){
        return (String) attributes.get("sub");
    }

    public String getFirstName(){
        return (String) attributes.get("given_name");
    }

    public String getLastName(){
        return (String) attributes.get("family_name");
    }

    public String getEmail(){
        return (String) attributes.get("email");
    }

    public String getImageUrl(){
        return (String) attributes.get("picture");
    }
}
