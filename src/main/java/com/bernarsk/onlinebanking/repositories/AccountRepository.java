package com.bernarsk.onlinebanking.repositories;
import com.bernarsk.onlinebanking.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AccountRepository  extends JpaRepository<Account, UUID>  {
    boolean existsByAccountNumber(String accountNumber);
    Account findByAccountNumber(String accountNumber);
    List<Account> findByUserId(@Param("id") UUID id);

}
