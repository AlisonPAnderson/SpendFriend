
package net.alisonanderson.ashleypeebles.spendfriendbackend.controller;

import net.alisonanderson.ashleypeebles.spendfriendbackend.business.*;
import net.alisonanderson.ashleypeebles.spendfriendbackend.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;


@RestController
@PreAuthorize("hasRole('ROLE_USER')")
//@PreAuthorize(authentication.principal.name=#username)
//@PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.principal.username")



public class AccountController {

    AccountService accountService;
    UserService userService;
    TransferService transferService;

    @Autowired
    public AccountController(AccountService accountService, UserService userService, TransferService transferService) {
        this.accountService = accountService;
        this.userService = userService;
        this.transferService = transferService;
    }

    @GetMapping("account/{accountId}/balance")
    public BigDecimal getBalance(@PathVariable long accountId, Principal principal) {
        return accountService.getBalance(accountId);
    }

    @GetMapping("account/{accountId}")
    public Account findByAccountId (@PathVariable long accountId) {
        return accountService.findByAccountId(accountId);
    }

    @GetMapping("users")
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("user/{userId}")
    public User findByUserId (@PathVariable long userId) {
        return userService.findByUserId(userId);
    }

    @GetMapping("user/{userId}/account")
    public Account findAccountByUserId (@PathVariable long userId) {
        return accountService.findByUserId(userId);
    }

    @GetMapping("/user/principal")
    public String findPrincipal(Principal principal) {
        return principal.getName();
    }
}
