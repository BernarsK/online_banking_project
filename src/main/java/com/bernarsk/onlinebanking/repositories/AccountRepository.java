package com.bernarsk.onlinebanking.repositories;
import com.bernarsk.onlinebanking.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository  extends JpaRepository<Transaction, Long>  {

}
