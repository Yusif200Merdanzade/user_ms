package az.company.userms;

import az.company.userms.entity.Account;
import az.company.userms.model.AccountDto;
import az.company.userms.repository.AccountRepository;
import az.company.userms.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AccountTest {

    @Autowired
    private UserService accountService;

    @MockBean
    private AccountRepository accountRepository;

    @Test
    public void getActiveAccounts_success() {
        Account account1 = new Account();
        account1.setStatus('1');
        account1.setUserId(1L);

        Account account2 = new Account();
        account2.setStatus('1');
        account2.setUserId(2L);

        List<Account> activeAccounts = Arrays.asList(account1, account2);

        Mockito.when(accountRepository.findByUserIdAndStatus(1L, '1')).thenReturn(activeAccounts);

        List<AccountDto> accounts = accountService.getActiveAccounts(1L);
        assertEquals(2, accounts.size());
    }
}
