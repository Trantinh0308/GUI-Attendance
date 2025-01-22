package com.example.attendance.repository;

import com.example.attendance.model.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface AccountRepository extends CrudRepository<Account, Long>{
	Account getAccountByUsernameAndPassword(String username,String password);
	Account findByUsername(String username);
}
