package com.koko.customer;

import com.koko.jwt.JWTUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    public final CustomerService customerService;
    private final JWTUtil jwtUtil;

    public CustomerController(CustomerService customerService, JWTUtil jwtUtil) {
        this.customerService = customerService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public List<CustomerDTO> getCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{customerId}")
    public CustomerDTO getCustomer(
            @PathVariable("customerId") Integer customerId) {
        return customerService.getCustomerById(customerId);
    }

    @PostMapping
    public ResponseEntity<?> registerCustomer(@RequestBody CustomerRegistrationRequest customerRegistrationRequest) {
        customerService.addCustomer(customerRegistrationRequest);
        String jwtToken = this.jwtUtil.issueToken(customerRegistrationRequest.email(), "ROLE_USER");
        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .build();
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
