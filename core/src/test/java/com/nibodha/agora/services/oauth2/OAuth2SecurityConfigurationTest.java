/*
 * Copyright 2016 Nibodha Technologies Pvt. Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nibodha.agora.services.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nibodha.agora.services.oauth2.support.AccessToken;
import com.nibodha.agora.services.oauth2.support.MockClientDetailsService;
import com.nibodha.agora.services.oauth2.support.OAuth2TestSecurityConfiguration;
import java.util.Collections;
import java.util.Set;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigWebContextLoader.class,
        classes = {OAuth2TestSecurityConfiguration.class})
@WebAppConfiguration
public class OAuth2SecurityConfigurationTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Set<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    private final User user = new User("test", "test", authorities);
    private final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, "test", authorities);

    @Inject
    private WebApplicationContext wac;

    @Inject
    @Qualifier("defaultAuthorizationServerTokenServices")
    private ResourceServerTokenServices tokenServices;

    private MockMvc mockMvc;


    @Before
    public void setup() {
        final OAuth2AuthenticationManager manager = new OAuth2AuthenticationManager();
        manager.setClientDetailsService(new MockClientDetailsService());
        manager.setTokenServices(tokenServices);

        final OAuth2AuthenticationProcessingFilter filter = new OAuth2AuthenticationProcessingFilter();
        filter.setAuthenticationManager(manager);

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilters(filter)
                .build();
    }

    @Test
    public void returnsOAuth2TokenForPasswordGrantType() throws Exception {
        this.mockMvc.perform(post("/oauth/token?grant_type=password&username=user&password=test").principal(token).
                header("Authorization", "Basic dGVzdDp0ZXN0"))
                .andExpect(status().isOk());
    }

    @Test
    public void authorisationFailsForInvalidPassword() throws Exception {
        this.mockMvc.perform(post("/oauth/token?grant_type=password&username=user&password=test1").principal(token).
                header("Authorization", "Basic dGVzdDp0ZXN0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void authorisationFailsForInvalidUser() throws Exception {
        this.mockMvc.perform(post("/oauth/token?grant_type=password&username=user1&password=test").principal(token).
                header("Authorization", "Basic dGVzdDp0ZXN0"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void authorisationFailsWhenPasswordIsMissing() throws Exception {
        this.mockMvc.perform(post("/oauth/token?grant_type=password&username=user&password1=test").principal(token).
                header("Authorization", "Basic dGVzdDp0ZXN0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void returnsOAuth2TokenForClientCredentialsGrantType() throws Exception {
        this.mockMvc.perform(post("/oauth/token?grant_type=client_credentials&client_id=test").principal(token).
                header("Authorization", "Basic dGVzdDp0ZXN0"))
                .andExpect(status().isOk());
    }


    @Test
    public void authorisationFailsForInvalidClientId() throws Exception {
        this.mockMvc.perform(post("/oauth/token?grant_type=client_credentials&client_id=test1").principal(token).
                header("Authorization", "Basic dGVzdDp0ZXN0"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void authorisationFailsForInvalidGrantType() throws Exception {
        this.mockMvc.perform(post("/oauth/token?grant_type=client_credentials1&client_id=test").principal(token)
                .header("Authorization", "Basic dGDzdDp0ZXN0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void authorisationFailsWhenPrincipalIsNotProvided() throws Exception {
        this.mockMvc.perform(post("/oauth/token?grant_type=client_credentials&client_id=test")
                .header("Authorization", "Basic dGDzdDp0ZXN0"))
                .andExpect(status().isUnauthorized());
    }


    @Test
    public void controllerFailsForInvalidAccessToken() throws Exception {
        this.mockMvc.perform(get(("/test?access_token=111")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void controllerSucceedsForValidAccessToken() throws Exception {
        final MvcResult result = this.mockMvc.perform(post("/oauth/token?grant_type=client_credentials&client_id=test").principal(token)
                .header("Authorization", "Basic dGDzdDp0ZXN0"))
                .andReturn();

        assertNotNull(result);
        assertNotNull(result.getResponse());
        final String value = result.getResponse().getContentAsString();
        assertNotNull(value);

        final AccessToken token = objectMapper.readValue(value, AccessToken.class);

        this.mockMvc.perform(get(("/test?access_token=" + token.getAccessToken())))
                .andExpect(status().isOk()).andExpect(content().string("success"));
    }
}