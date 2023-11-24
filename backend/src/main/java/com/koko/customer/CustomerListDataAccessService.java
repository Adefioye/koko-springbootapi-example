package com.koko.customer;

import com.koko.exception.DuplicateResourceException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDao{

    // DB
    public static List<Customer> customers;

    static {
        customers = new ArrayList<>();
        Customer alex = new Customer(1, "Alex", "alex@gmail.com", 21, Gender.MALE);
        Customer jamila = new Customer(2, "Jamila", "jamila@gmail.com", 19, Gender.MALE);
        customers.add(alex);
        customers.add(jamila);
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        return customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    @Override
    public void removeCustomerById(Integer id) {

    }

    @Override
    public void updateCustomer(Customer customer) {

    }

    @Override
    public void insertCustomer(Customer customer) {
        // Check of email exist, if not then add
        if (existsCustomerEmail(customer.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        customers.add(customer);

    }

    @Override
    public boolean existsCustomerEmail(String email) {
        return customers.stream()
                .anyMatch(c -> c.getEmail().equals(email));
    }

    @Override
    public boolean existsCustomerId(Integer id) {
        return false;
    }


}
