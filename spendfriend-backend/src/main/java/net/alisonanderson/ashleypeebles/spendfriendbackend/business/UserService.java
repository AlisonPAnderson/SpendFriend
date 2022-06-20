package net.alisonanderson.ashleypeebles.spendfriendbackend.business;

import net.alisonanderson.ashleypeebles.spendfriendbackend.dao.AccountRepository;
import net.alisonanderson.ashleypeebles.spendfriendbackend.dao.UserRepository;
import net.alisonanderson.ashleypeebles.spendfriendbackend.model.Account;
import net.alisonanderson.ashleypeebles.spendfriendbackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    AccountRepository accountRepository;

    private final BigDecimal STARTING_BALANCE = new BigDecimal("1000.00");

    public User findByUserId(long userId) {
        return userRepository.findById(userId);
    }

    public User findByUsername(String username) throws UsernameNotFoundException{
        try {
            //            foundUser.setActivated(true);
//            foundUser.setAuthorities("USER
            return userRepository.findByUsername(username);
        } catch (Exception e) {
            throw new UsernameNotFoundException("User '" + username + "' was not found.");
        }
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }
//    public boolean isRegustered() {
//        return false;
//    }

    public boolean isNotRegistered() {
        return true;
    }
    public String create(String username, String password) {

        User newUser = new User();
        String password_hash = new BCryptPasswordEncoder().encode(password);
        newUser.setUsername(username);
        newUser.setPassword(password_hash);
        newUser.setActivated(true);
        newUser.setAuthorities("USER");
        try {
            userRepository.saveAndFlush(newUser);
        } catch (Exception e) {
            isNotRegistered();
            return "/register";
        }
        Account newAccount = new Account();
        newAccount.setId(newAccount.getId());
        newAccount.setUserId(newUser.getId());
        newAccount.setBalance(STARTING_BALANCE);
        try {
            accountRepository.saveAndFlush(newAccount);
        } catch (Exception e) {
            isNotRegistered();
            return "/register";
        }
        return "redirect:/registration?success";
    }

}
