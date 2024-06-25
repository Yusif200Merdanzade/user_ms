package az.company.userms.mapper;


import az.company.userms.entity.Account;
import az.company.userms.model.AccountDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class AccountMapper {

    public abstract AccountDto entitiyToDto(Account entities);
}
