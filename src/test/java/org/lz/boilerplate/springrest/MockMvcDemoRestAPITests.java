
package org.lz.boilerplate.springrest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MockMvcDemoRestAPITests {
    public static final String API_DEMO_HELLO = "/api/demo/hello";
    public static final String OAUTH_END_POINT = "/oauth/token";
    public static final String HELLO_API_EXPECTED = "helloworld";
    public static final String OAUTH_LZHENG_CLIENT = "lzheng-client";
    public static final String OAUTH_LZHENG_SECRET = "lzheng-secret";

    @Autowired
    private MockMvc mvc;

    @Test
    public void callHelloFailedWithNoCredential() throws Exception {
        this.mvc.perform(get(API_DEMO_HELLO)).andExpect(status().isUnauthorized());
    }

    @Test
    public void callHelloOkWithBasicAuth() throws Exception {

        this.mvc.perform(get(API_DEMO_HELLO).with(httpBasic("user", "password")))
                .andExpect(status().isOk())
                .andExpect(content().string(HELLO_API_EXPECTED));
    }

    @Test
    public void getTokenWithBasicAuth() throws Exception {
        this.mvc.perform(post(OAUTH_END_POINT)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .params(getOAuthFormData())
                .with(httpBasic("lzheng-client", "lzheng-secret")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("access_token")));
    }

    @Test
    public void getTokenWithoutBasicAuth() throws Exception {
        this.mvc.perform(post(OAUTH_END_POINT)
                .with(httpBasic("", "")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void callHelooWithOAuth() throws Exception {
        ResultActions result = this.mvc.perform(post(OAUTH_END_POINT)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .params(getOAuthFormData())
                .with(httpBasic(OAUTH_LZHENG_CLIENT, OAUTH_LZHENG_SECRET)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("access_token")));

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        String token = jsonParser.parseMap(resultString).get("access_token").toString();

        this.mvc.perform(get(API_DEMO_HELLO)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string(HELLO_API_EXPECTED));
    }

    private MultiValueMap<String, String> getOAuthFormData() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
        map.add("username", "user");
        map.add("password", "password");
        return map;
    }
}

