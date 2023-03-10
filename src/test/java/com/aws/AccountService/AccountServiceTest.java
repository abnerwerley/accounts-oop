package com.aws.AccountService;

import com.aws.account.entity.Account;
import com.aws.account.persistence.AccountRepository;
import com.aws.account.service.AccountService;
import com.aws.client.entity.Client;
import com.aws.exception.RequestException;
import com.aws.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @InjectMocks
    private AccountService service;

    @Mock
    private AccountRepository repository;

    public static final Long ID = 9L;
    public static final Long ID_TO = 10L;
    public static final double BALANCE = 1500.0;
    public static final int AGENCY = 123;
    public static final int NUMBER = 123456;
    public static final Client OWNER = new Client();
    public static final int TOTAL = 0;
    public static final double VALUE = 100.0;
    public static final double HUGE_VALUE = 10000000.0;

    @Test
    void testDeposit() {
        Account account = new Account(ID, BALANCE, AGENCY, NUMBER, OWNER);

        doReturn(getOptionalAccount()).when(repository).findById(ID);
        double shouldDeposit = service.deposit(account, VALUE);
        assertEquals(1600.0, shouldDeposit);

        doThrow(ResourceNotFoundException.class).when(repository).findById(ID);
        Exception notFoundException = assertThrows(ResourceNotFoundException.class, () -> service.deposit(account, VALUE));
        assertNotNull(notFoundException);

        doReturn(getOptionalAccount()).when(repository).findById(ID);
        doThrow(IllegalArgumentException.class).when(repository).save(account);
        Exception exception = assertThrows(RequestException.class, () -> service.deposit(account, VALUE));
        assertNotNull(exception);
        assertEquals("Could not deposit.", exception.getMessage());
    }

    @Test
    void testDraw() {
        Account account = new Account(ID, BALANCE, AGENCY, NUMBER, OWNER);

        doReturn(getOptionalAccount()).when(repository).findById(ID);
        double shouldDraw = service.draw(account, VALUE);
        assertEquals(1400.0, shouldDraw);

        doReturn(getOptionalAccount()).when(repository).findById(ID);
        Exception shouldNotDraw = assertThrows(RequestException.class, () -> service.draw(account, HUGE_VALUE));
        assertNotNull(shouldNotDraw);

        doThrow(ResourceNotFoundException.class).when(repository).findById(ID);
        Exception notFoundException = assertThrows(ResourceNotFoundException.class, () -> service.draw(account, VALUE));
        assertNotNull(notFoundException);

        doReturn(getOptionalAccount()).when(repository).findById(ID);
        doThrow(IllegalArgumentException.class).when(repository).save(account);
        Exception exception = assertThrows(RequestException.class, () -> service.draw(account, VALUE));
        assertNotNull(exception);
        assertEquals("Could not draw.", exception.getMessage());
    }

    @Test
    void testTransfer() {
        Account accountFrom = new Account(ID, BALANCE, AGENCY, NUMBER, OWNER);
        Account accountTo = new Account(ID_TO, BALANCE, AGENCY, NUMBER, OWNER);

        doReturn(getOptionalAccount()).when(repository).findById(ID);
        doReturn(getOptionalAccount()).when(repository).findById(ID_TO);
        boolean response = service.transfer(accountFrom, accountTo, VALUE);
        assertTrue(response);

        doReturn(getOptionalAccount()).when(repository).findById(ID);
        doReturn(getOptionalAccount()).when(repository).findById(ID_TO);
        boolean shouldNotTransfer = service.transfer(accountFrom, accountTo, HUGE_VALUE);
        assertFalse(shouldNotTransfer);

        doThrow(ResourceNotFoundException.class).when(repository).findById(ID);
        Exception notFoundException = assertThrows(ResourceNotFoundException.class, () -> service.transfer(
                accountFrom, accountTo, VALUE));
        assertNotNull(notFoundException);
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
