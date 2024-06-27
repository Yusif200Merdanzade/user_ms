package az.company.userms.service;

import az.company.userms.entity.Account;
import az.company.userms.exception.NotFoundException;
import az.company.userms.model.TransferDto;
import az.company.userms.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final AccountRepository accountRepository;

    public void transfer(TransferDto dto) throws NotFoundException {

        Account fromAccount = accountRepository.findById(dto.getFromAccountId())
                .orElseThrow(() -> new NotFoundException("From account does not exist"));
        Account toAccount = accountRepository.findById(dto.getToAccountId())
                .orElseThrow(() -> new NotFoundException("To account does not exist"));

        if (fromAccount.getId().equals(toAccount.getId())) {
            throw new NotFoundException("Cannot transfer to the same account");
        }

        if (toAccount.getStatus() != '1') {
            throw new NotFoundException("Destination account is deactive");
        }

        if (fromAccount.getBalance().compareTo(dto.getAmount().toString()) < 0) {
            throw new NotFoundException("Insufficient balance");
        }

        fromAccount.setBalance(dto.getAmount().toString());
        toAccount.setBalance(dto.getAmount().toString());

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }
}
