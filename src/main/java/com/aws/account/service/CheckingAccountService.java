package com.aws.account.service;

import com.aws.account.entity.Account;
import com.aws.account.persistence.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class CheckingAccountService extends AccountService {

    public CheckingAccountService(AccountRepository repository) {
        super(repository);
    }

    @Override
    public double draw(Account account, double value) {
        return super.draw(account, value + 0.2);
    }
}
