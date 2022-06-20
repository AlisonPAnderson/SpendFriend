package net.alisonanderson.ashleypeebles.spendfriendbackend.controller;


import net.alisonanderson.ashleypeebles.spendfriendbackend.business.UserService;
import net.alisonanderson.ashleypeebles.spendfriendbackend.dao.UserRepository;
import net.alisonanderson.ashleypeebles.spendfriendbackend.model.LoginDTO;
import net.alisonanderson.ashleypeebles.spendfriendbackend.model.RegisterUserDTO;
import net.alisonanderson.ashleypeebles.spendfriendbackend.model.User;
import net.alisonanderson.ashleypeebles.spendfriendbackend.security.jwt.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.List;


/**
 * Controller to authenticate users.
 */
@RestController
public class AuthenticationController {

    private final TokenProvider tokenProvider;

    @Autowired
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    private final UserService userService;

    @Autowired
    public AuthenticationController(TokenProvider tokenProvider,
                                    AuthenticationManagerBuilder
                                            authenticationManagerBuilder,
                                    UserRepository userRepository,
                                    UserService userService) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        //this.userDao = userDao;
        this.userService = userService;
    }

    @PermitAll
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public LoginResponse login(/* @Valid */ @RequestBody LoginDTO loginDto) {

        User user = userService.findByUsername(loginDto.getUsername());
        user.setAuthorities("USER");

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication, false);

        return new LoginResponse(jwt, user);
    }

    @PermitAll
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    //@RequestMapping(value = "/register", method = RequestMethod.POST)
    public void register(@Valid @RequestBody RegisterUserDTO newUser) {
        User existingUser = userService.findByUsername(newUser.getUsername());
        if(userService.isNotRegistered()) {
//        if(!userService.create(newUser.getUsername(), newUser.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User registration failed.");
        }
    }

    @PermitAll
    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    @PermitAll
    @GetMapping("/{username}")
    public User findByUsername(@PathVariable String username) {
        return userService.findByUsername(username);
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class LoginResponse {

        private String token;
        private User user;

        LoginResponse(String token, User user) {
            this.token = token;
            this.user = user;
        }

        public String getToken() {
            return token;
        }

        void setToken(String token) {
            this.token = token;
        }

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}
    }
}

