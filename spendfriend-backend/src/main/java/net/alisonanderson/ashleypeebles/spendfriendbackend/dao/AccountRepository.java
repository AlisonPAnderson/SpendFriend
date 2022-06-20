package net.alisonanderson.ashleypeebles.spendfriendbackend.dao;

import net.alisonanderson.ashleypeebles.spendfriendbackend.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    BigDecimal findBalanceByUserId(long id);

    Account findByUserId(long id);

    Account findById(long id);

    List<Account> findAll();


}
