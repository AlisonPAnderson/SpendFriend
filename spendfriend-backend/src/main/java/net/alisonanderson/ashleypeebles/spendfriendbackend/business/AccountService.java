package net.alisonanderson.ashleypeebles.spendfriendbackend.business;

import lombok.AllArgsConstructor;
import net.alisonanderson.ashleypeebles.spendfriendbackend.dao.AccountRepository;
import net.alisonanderson.ashleypeebles.spendfriendbackend.dao.UserRepository;
import net.alisonanderson.ashleypeebles.spendfriendbackend.exceptions.NonSufficentFundsException;
import net.alisonanderson.ashleypeebles.spendfriendbackend.model.Account;
import net.alisonanderson.ashleypeebles.spendfriendbackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
@AllArgsConstructor
public class AccountService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserRepository userRepository;

   public Account findByAccountId (long accountId) { return accountRepository.findById(accountId); }

   public Account findByUserId (long id) {
        return accountRepository.findByUserId(id);
    }

    List<User> findAllUsers() {
       return userRepository.findAll();
    }

    public BigDecimal getBalance(long accountId) {
        return accountRepository.findById(accountId).getBalance();
    }

    public BigDecimal addToBalance(long accountId, BigDecimal amount) {
       return getBalance(accountId).add(amount);
    }

    public BigDecimal subtractFromBalance(long accountId, BigDecimal amount) throws NonSufficentFundsException {

            if (getBalance(accountId).compareTo(amount) > 0) {
                return getBalance(accountId).subtract(amount);
            } else throw new NonSufficentFundsException();
    }

    public Account updateBalance(Account account, BigDecimal newBalance) {
       account.setBalance(newBalance);
        return account;
    }

}
