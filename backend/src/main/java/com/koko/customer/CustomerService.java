package com.koko.customer;

import com.koko.exception.DuplicateResourceException;
import com.koko.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerDao customerDao;
    private final PasswordEncoder passwordEncoder;
    private final CustomerDTOMapper customerDTOMapper;

    public CustomerService(PasswordEncoder passwordEncoder,
                           CustomerDTOMapper customerDTOMapper,
                           @Qualifier("jdbc") CustomerDao customerDao) {
        this.customerDao = customerDao;
        this.customerDTOMapper = customerDTOMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<CustomerDTO> getAllCustomers() {
        return customerDao
                .selectAllCustomers()
                .stream()
                .map(customerDTOMapper)
                .collect(Collectors.toList());
    }

    public CustomerDTO  getCustomerById(Integer id) {
        return customerDao
                .selectCustomerById(id)
                .map(customerDTOMapper)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Customer with id [%s] not found!".formatted(id))
                );
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        // Check if email exists, if not, add customerregistrationrequest
        if (customerDao.existsCustomerEmail(customerRegistrationRequest.email())) {
            throw new DuplicateResourceException("Email already exists");
        }
        Customer customer = new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                passwordEncoder.encode(customerRegistrationRequest.password()),
                customerRegistrationRequest.age(),
                customerRegistrationRequest.gender()
        );
        customerDao.insertCustomer(customer);
    }

    void deleteCustomerById(Integer id) {

        if (!customerDao.existsCustomerId(id)) {
            throw new ResourceNotFoundException("Customer with [%s] not found!".formatted(id));
        }
        customerDao.removeCustomerById(id);
    }

    void updateCustomer(Integer id, CustomerRegistrationRequest customerUpdateRequest) {

        Customer customer = customerDao
                .selectCustomerById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "customer with id [%s] not found".formatted(id)
                ));


        boolean hasCustomerChanged = false;

        // Check if name has changed
        if (customerUpdateRequest.name() != null && !customer.getName().equals(customerUpdateRequest.name())) {
            customer.setName(customerUpdateRequest.name());
            hasCustomerChanged = true;
        }

        // Check if age has changed
        if (customerUpdateRequest.age() != null && !customer.getAge().equals(customerUpdateRequest.age())) {
            customer.setAge(customerUpdateRequest.age());
            hasCustomerChanged = true;
        }

        // Check if gender has changed
        if (customerUpdateRequest.gender() != null && !customer.getGender().equals(customerUpdateRequest.gender())) {
            customer.setGender(customerUpdateRequest.gender());
            hasCustomerChanged = true;
        }

        // Check if email has changed
        if (customerUpdateRequest.email() != null && !customer.getEmail().equals(customerUpdateRequest.email())) {
            if (customerDao.existsCustomerEmail(customerUpdateRequest.email())) {
                throw new DuplicateResourceException("Email already exists!");
            }
            customer.setEmail(customerUpdateRequest.email());
            hasCustomerChanged = true;
        }

        if (!hasCustomerChanged) {
            throw new DuplicateResourceException("Try again, customer info has not changed");
        }

        customerDao.updateCustomer(customer);
    }
}
