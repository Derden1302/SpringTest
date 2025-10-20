package ru.springtest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;
import ru.springtest.domain.Accounts;
import ru.springtest.domain.Contracts;
import ru.springtest.domain.Customers;
import ru.springtest.dto.AccountCreateUpdateDto;
import ru.springtest.dto.CustomerAccountCreateUpdateDto;
import ru.springtest.dto.CustomerAccountResponseDto;
import ru.springtest.dto.CustomerCreateUpdateDto;


@Mapper(componentModel = "spring")
public interface CustomerAccountMapper{
    @Mapping(target = "customerId", source = "customers.id")
    @Mapping(target = "name", source = "customers.name")
    @Mapping(target = "accountData", source = "accounts.accountData")
    CustomerAccountResponseDto toDto(Customers customers, Accounts accounts);

    Customers createCustomer(CustomerCreateUpdateDto dto);

    Customers createCustomer(CustomerAccountCreateUpdateDto dto);

    Accounts createAccount(AccountCreateUpdateDto dto, Customers customers);

    Accounts createAccount(CustomerAccountCreateUpdateDto dto, Customers customers);

    void updateAccount(@MappingTarget Accounts accounts, AccountCreateUpdateDto accountCreateUpdateDto);

    void updateAccount(@MappingTarget Accounts accounts, CustomerAccountCreateUpdateDto dto);

    void updateCustomer(@MappingTarget Customers customers, CustomerCreateUpdateDto customerCreateUpdateDto);

    void updateCustomer(@MappingTarget Customers customers, CustomerAccountCreateUpdateDto dto);

}