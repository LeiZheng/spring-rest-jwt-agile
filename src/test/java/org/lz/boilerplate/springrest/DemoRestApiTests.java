package org.lz.boilerplate.springrest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DemoRestApiTests {

    public static final String API_DEMO_HELLO = "/api/demo/hello";
    public static final String HELLO_API_EXPECTED = "helloworld";
    public static final String OAUTH_END_POINT = "/oauth/token";
    @LocalServerPort
    int randomPort;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void callHelloApiFailIfNoCredential() {
        ResponseEntity<String> resp = this.restTemplate
                .exchange(API_DEMO_HELLO, HttpMethod.GET, null, String.class );
        Assert.assertEquals(HttpStatus.UNAUTHORIZED, resp.getStatusCode());
    }

    @Test
    public void callHelloApiOkWithBasicAuth() {

        ResponseEntity<String> resp = this.restTemplate.withBasicAuth("user", "password")
                .exchange(API_DEMO_HELLO, HttpMethod.GET, null, String.class );
        Assert.assertEquals(HttpStatus.OK, resp.getStatusCode());
        Assert.assertEquals(HELLO_API_EXPECTED, resp.getBody());
    }


    @Test
    public void getTokenWithBasicAuth() {
        // $ curl <client-id> : <client-secret>@<api-end-point-url >/oauth/token
        // -d grant_type=password -d username=<username> -d password=< password >

        ResponseEntity<String> resp = this.restTemplate.withBasicAuth("lzheng-client", "lzheng-secret")
                .exchange(OAUTH_END_POINT, HttpMethod.POST, getOAuthFormData(), String.class);
        Assert.assertEquals(HttpStatus.OK, resp.getStatusCode());
        Assert.assertTrue("contain access_token string", resp.getBody().contains("access_token"));
    }

    @Test
    public void getTokenWithoutBasicAuth() {
        ResponseEntity<String> resp = this.restTemplate.withBasicAuth("", "")

                .exchange(OAUTH_END_POINT, HttpMethod.POST, getOAuthFormData(), String.class );
        Assert.assertEquals(HttpStatus.UNAUTHORIZED, resp.getStatusCode());

    }


}

