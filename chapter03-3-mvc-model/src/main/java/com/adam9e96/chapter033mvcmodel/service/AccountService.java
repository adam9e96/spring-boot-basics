package com.adam9e96.chapter033mvcmodel.service;

import com.adam9e96.chapter033mvcmodel.entity.Account;
import com.adam9e96.chapter033mvcmodel.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    public Account create(Account account) {
        return accountRepository.save(account);
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Account findById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정입니다: " + id));
    }

    @Transactional
    public Account update(Long id, Account form) {
        Account account = findById(id);
        account.setName(form.getName());
        account.setPhone(form.getPhone());
        account.setAddress(form.getAddress());
        account.setPrivacyAgreement(form.getPrivacyAgreement());
        return account;
    }

    @Transactional
    public void delete(Long id) {
        accountRepository.deleteById(id);
    }
}
