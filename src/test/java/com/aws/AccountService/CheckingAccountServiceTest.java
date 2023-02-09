package com.aws.AccountService;

import com.aws.account.entity.Account;
import com.aws.account.persistence.AccountRepository;
import com.aws.account.service.CheckingAccountService;
import com.aws.client.entity.Client;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class CheckingAccountServiceTest {

    @InjectMocks
    private CheckingAccountService service;

    @Mock
    private AccountRepository repository;

    public static final Long ID = 9L;
    public static final double BALANCE = 1000.0;
    public static final int AGENCY = 123;
    public static final int NUMBER = 123456;
    public static final Client OWNER = new Client();
    public static final double VALUE = 100.0;

    @Test
    void testDraw() {
        Account account = new Account(ID, BALANCE, AGENCY, NUMBER, OWNER);

        doReturn(getOptionalAccount()).when(repository).findById(ID);
        double shouldDeposit = service.draw(account, VALUE);
        assertEquals(899.8, shouldDeposit);
    }


    public Optional<Account> getOptionalAccount() {
        return Optional.of(
                Account.builder()
                        .id(ID)
                        .agency(AGENCY)
                        .balance(BALANCE)
                        .number(NUMBER)
                        .owner(OWNER)
                        .build());
    }
}
