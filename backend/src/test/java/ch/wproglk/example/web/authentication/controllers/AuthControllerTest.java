package ch.wproglk.example.web.authentication.controllers;

import ch.wproglk.example.data.role.RoleDao;
import ch.wproglk.example.data.user.User;
import ch.wproglk.example.data.user.UserDao;
import ch.wproglk.example.model.Role;
import ch.wproglk.example.web.authentication.payload.request.LoginRequest;
import ch.wproglk.example.web.authentication.payload.request.SignupRequest;
import ch.wproglk.example.web.utilities.ControllerTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Set;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresent;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = {AuthController.class, TestController.class})
class AuthControllerTest extends ControllerTest
{
    @Autowired
    MockMvc mvc;

    @Autowired
    UserDao userDao;

    @Autowired
    RoleDao roleDao;

    @Test
    void signup() throws Exception
    {
        SignupRequest request = new SignupRequest();
        request.setUsername("user");
        request.setPassword("password");
        request.setEmail("user@example.com");
        request.setRole(Set.of(Role.USER.name()));

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        String body = writer.writeValueAsString(request);
        mvc.perform(post("/api/auth/signup").contentType(MediaType.APPLICATION_JSON).content(body))
           .andExpect(status().isOk());

        assertThat(userDao.findByUsername(request.getUsername()), isPresent());
    }

    @Test
    void signin() throws Exception
    {
        // Setup
        User user = new User();
        user.setUsername("admin");
        user.setPassword("{noop}admin");  //prefix {noop} makes sure to use NoopPasswordDecoder
        user.setEmail("admin@example.com");
        user.setRoles(Set.of(roleDao.findByName(Role.ADMIN).get()));

        userDao.save(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(user.getUsername());
        loginRequest.setPassword("admin");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        String body = writer.writeValueAsString(loginRequest);

        // Test
        MvcResult result = mvc.perform(post("/api/auth/signin").contentType(MediaType.APPLICATION_JSON).content(body))
                              .andExpect(status().isOk())
                              .andExpect(jsonPath("$.accessToken", is(not(emptyString()))))
                              .andReturn();

        DocumentContext doc = JsonPath.parse(result.getResponse().getContentAsString());
        String token = doc.read("$.accessToken");

        Cookie authenticationCookie = new Cookie("auth.access_token", token);
        mvc.perform(get("/api/test/admin").contentType(MediaType.APPLICATION_JSON)
                                        .cookie(authenticationCookie))
           .andExpect(status().isOk());
    }
}