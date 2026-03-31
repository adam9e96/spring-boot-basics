package com.adam9e96.chapter033mvcmodel.repository;

import com.adam9e96.chapter033mvcmodel.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
