package ru.springtest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import ru.springtest.domain.Account;
import ru.springtest.domain.Customer;
import ru.springtest.dto.AccountCreateUpdateDto;
import ru.springtest.dto.CustomerAccountCreateUpdateDto;
import ru.springtest.dto.CustomerAccountResponseDto;
import ru.springtest.dto.CustomerCreateUpdateDto;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerAccountMapper{
    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "name", source = "customer.name")
    @Mapping(target = "accountData", source = "account.accountData")
    CustomerAccountResponseDto toDto(Customer customer, Account account);

    Customer toEntity(CustomerCreateUpdateDto dto);

    Customer toEntity(CustomerAccountCreateUpdateDto dto);

    Account toEntity(AccountCreateUpdateDto dto, Customer customer);

    Account toEntity(CustomerAccountCreateUpdateDto dto, Customer customer);

    void changeAccount(@MappingTarget Account account, AccountCreateUpdateDto accountCreateUpdateDto);

    void changeAccount(@MappingTarget Account account, CustomerAccountCreateUpdateDto dto);

    void changeCustomer(@MappingTarget Customer customer, CustomerCreateUpdateDto customerCreateUpdateDto);

    void changeCustomer(@MappingTarget Customer customer, CustomerAccountCreateUpdateDto dto);

}