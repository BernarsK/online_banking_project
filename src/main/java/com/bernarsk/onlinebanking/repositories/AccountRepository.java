package com.bernarsk.onlinebanking.repositories;
import com.bernarsk.onlinebanking.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository  extends JpaRepository<Account, Long>  {
    boolean existsByAccountNumber(String accountNumber);
    Account findByAccountNumber(String accountNumber);

}
