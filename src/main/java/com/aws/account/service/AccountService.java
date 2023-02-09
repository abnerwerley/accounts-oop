package com.aws.account.service;

import com.aws.account.entity.Account;
import com.aws.account.persistence.AccountRepository;
import com.aws.exception.RequestException;
import com.aws.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountService {

    @Autowired
    private AccountRepository repository;

    public static final String ACCOUNT_DOES_NOT_EXIST = "Account does not exist.";

    public double deposit(Account account, double value) {
        try {
            Optional<Account> owner = repository.findById(account.getId());
            if (owner.isEmpty()) {
                throw new ResourceNotFoundException(ACCOUNT_DOES_NOT_EXIST);
            }
            double balance = owner.get().getBalance();
            owner.get().setBalance(balance + value);
            repository.save(owner.get());
            return (balance + value);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new RequestException("Could not deposit.");
        }
    }

    public double draw(Account account, double value) {
        try {
            Optional<Account> owner = repository.findById(account.getId());
            if (owner.isEmpty()) {
                throw new ResourceNotFoundException(ACCOUNT_DOES_NOT_EXIST);
            }
            if (owner.get().getBalance() >= value) {
                owner.get().setBalance(owner.get().getBalance() - value);
                repository.save(owner.get());
                return owner.get().getBalance();
            }
            throw new RequestException("You do not have all this money to draw.");
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new RequestException("Could not draw.");
        }
    }

    public boolean transfer(Account from, Account to, double value) {
        try {
            Optional<Account> accountFrom = repository.findById(from.getId());
            Optional<Account> accountTo = repository.findById(to.getId());
            if (accountFrom.isEmpty() || accountTo.isEmpty()) {
                throw new ResourceNotFoundException(ACCOUNT_DOES_NOT_EXIST);
            }
            if (accountFrom.get().getBalance() >= value) {
                draw(accountFrom.get(), value);
                deposit(accountTo.get(), value);
                return true;
            }
            return false;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new RequestException("Could not Transfer.");
        }

    }
}
