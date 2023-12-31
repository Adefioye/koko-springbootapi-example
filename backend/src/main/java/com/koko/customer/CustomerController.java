package com.koko.customer;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    public final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{customerId}")
    public Customer getCustomer(
            @PathVariable("customerId") Integer customerId) {
        return customerService.getCustomerById(customerId);
    }

    @PostMapping
    public void registerCustomer(@RequestBody CustomerRegistrationRequest customerRegistrationRequest) {
        customerService.addCustomer(customerRegistrationRequest);
    }

    @DeleteMapping("/{customerId}")
    public void deleteCustomer(
            @PathVariable("customerId") Integer customerId
    ) {
        customerService.deleteCustomerById(customerId);
    }

    @PutMapping("/{customerId}")
    public void updateCustomer(
            @PathVariable("customerId") Integer customerId,
            @RequestBody CustomerRegistrationRequest customerUpdateRequest
    ) {
        System.out.println(customerId);
        System.out.println(customerUpdateRequest);
        customerService.updateCustomer(customerId, customerUpdateRequest);
    }
}
