package com.kyn.api.kynapi.Controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.kyn.api.kynapi.Model.Store;
import com.kyn.api.kynapi.Repository.StoreRepository;
import com.kyn.api.kynapi.Security.CustomerUserDetailsService;
import com.kyn.api.kynapi.Security.TokenProvider;
import com.kyn.api.kynapi.Security.oauth.CustomOAuth2UserService;
import com.kyn.api.kynapi.Security.oauth.OAuth2AuthenticationFailureHandler;
import com.kyn.api.kynapi.Security.oauth.OAuth2AuthenticationSuccessHandler;
import com.kyn.api.kynapi.Services.RoleService;
import com.kyn.api.kynapi.Services.StoreService;
import com.kyn.api.kynapi.Services.UserService;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;

@WebMvcTest(controllers = AuthApiController.class)
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class KynApiSearchStoreTest {
    
    @Autowired
	WebApplicationContext context;

    MockMvc mock;

    @MockBean
    StoreService storeService;

    @MockBean
    UserService userService;
    
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

    @Before
    public void setUp(){
        mock = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
	@WithMockUser(roles = "USER")
	public void searchStore() throws Exception{

        Store store1= new Store();
        store1.setLocations("Manila, Bulacan, Cebu");
        store1.setStoreEmail("johndanieltejero23@gmail.com");
        store1.setContactNo("+63 939 841 6091");
        store1.setStoreName("Andok's");
        store1.setStoreOwner("John Daniel Tejero");
        store1.setStoreId(1);

        Store store2= new Store();
        store2.setLocations("Manila, Bulacan");
        store2.setStoreEmail("testuser@gmail.com");
        store2.setContactNo("+63 939 841 6091");
        store2.setStoreName("Andok's");
        store2.setStoreOwner("Mr. K");
        store2.setStoreId(2);

		mock.perform(MockMvcRequestBuilders
        .get("/kynapi/auth/store-listing").param("search", "Mr. K"))
		.andExpect(MockMvcResultMatchers.status().isOk());
	}

    
}
