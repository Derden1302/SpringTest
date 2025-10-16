package ru.springtest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.springtest.domain.Customers;

import java.util.UUID;

@Repository
public interface CustomersRepository extends JpaRepository<Customers, UUID> {
    Customers findByCustomerName(String s);
}
