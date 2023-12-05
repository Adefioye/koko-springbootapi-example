package com.koko.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(Integer id);

    void removeCustomerById(Integer id);

    void updateCustomer(Customer customer);

    void insertCustomer(Customer customer);

    boolean existsCustomerEmail(String email);

    boolean existsCustomerId(Integer id);

    Optional<Customer> selectUserByEmail(String email);

}
