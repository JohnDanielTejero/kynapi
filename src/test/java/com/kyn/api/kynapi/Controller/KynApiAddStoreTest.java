package com.kyn.api.kynapi.Controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;

import java.nio.charset.Charset;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.kyn.api.kynapi.Model.Store;
import com.kyn.api.kynapi.Repository.StoreRepository;
import com.kyn.api.kynapi.Security.CustomerUserDetailsService;
import com.kyn.api.kynapi.Security.TokenProvider;
import com.kyn.api.kynapi.Security.oauth.CustomOAuth2UserService;
import com.kyn.api.kynapi.Security.oauth.OAuth2AuthenticationFailureHandler;
import com.kyn.api.kynapi.Security.oauth.OAuth2AuthenticationSuccessHandler;
import com.kyn.api.kynapi.Services.RoleService;
import com.kyn.api.kynapi.Services.StoreService;

@WebMvcTest(controllers = AdminApiController.class)
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class KynApiAddStoreTest {

    @Autowired
	WebApplicationContext context;

    MockMvc mock;

    @MockBean
    StoreService storeService;

    @MockBean
    private TokenProvider tokenProvider;
    
    @MockBean
    StoreRepository storeRepository;

    @MockBean
	CustomerUserDetailsService customUserDetailsService;

	@MockBean
	CustomOAuth2UserService customerOAuth2UserService;

    @MockBean
    OAuth2AuthenticationSuccessHandler auth2AuthenticationSuccessHandler;
    
    @MockBean
    OAuth2AuthenticationFailureHandler auth2AuthenticationFailureHandler;

    @MockBean
    RoleService roleService;

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    @Before
    public void setUp(){
        mock = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
	@WithMockUser(roles = "ADMIN")
	public void addStore() throws Exception{

        Store store = new Store();
        store.setLocations("Manila, Bulacan, Cebu");
        store.setStoreEmail("johndanieltejero23@gmail.com");
        store.setContactNo("+63 939 841 6091");
        store.setStoreName("Andok's");
        store.setStoreOwner("John Daniel Tejero");
        store.setStoreId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(store);

        System.out.println(requestJson);
		mock.perform(MockMvcRequestBuilders
        .post("/kynapi/admin/add-store")
        .contentType(APPLICATION_JSON_UTF8)
        .content(requestJson))
		.andExpect(MockMvcResultMatchers.status().isOk());
	}

//.header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMSIsImlhdCI6MTY2MzIwOTAxMiwiZXhwIjoxNjY0MDczMDEyfQ.GOzD4bejJEMXCUV7iUK7YTfrCJmis5sOkR0ERnfD35QG2tdUHbKUVKk3jAYGW1Wai6f1bEjHXxcKdc-fSIJVFg")
}
