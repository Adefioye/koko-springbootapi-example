package com.koko.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao {

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        var sql = """
                select id, name, email, password, age, gender
                from customer
                """;

        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {

        var sql = """
                select id, name, email, password, age, gender
                from customer
                where id = ?
                """;

        return jdbcTemplate
                .query(sql, customerRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public void removeCustomerById(Integer id) {

        var sql = """
                delete from customer
                where id = ?
                """;

        jdbcTemplate.update(sql, id);

    }

    @Override
    public void updateCustomer(Customer customer) {
        // Update the name
        if (customer.getName() != null) {
            var sql = "update customer set name = ? where id = ?";
            jdbcTemplate.update(sql, customer.getName(), customer.getId());
        }

        // Update the email
        if (customer.getEmail() != null) {
            var sql = "update customer set email = ? where id = ?";
            jdbcTemplate.update(sql, customer.getEmail(), customer.getId());
        }

        // Update the age
        if (customer.getAge() != null) {
            var sql = "update customer set age = ? where id = ?";
            jdbcTemplate.update(sql, customer.getAge(), customer.getId());
        }

    }

    @Override
    public void insertCustomer(Customer customer) {

        var sql = """
                insert into customer(name, email, password, age, gender)
                values(?, ?, ?, ?, ?)
                """;

        int update = jdbcTemplate.update(
                sql,
                customer.getName(),
                customer.getEmail(),
                customer.getPassword(),
                customer.getAge(),
                customer.getGender().toString()
        );

        System.out.println("JdbcTemplate Result: " + update);
    }

    @Override
    public boolean existsCustomerEmail(String email) {

        var sql = """
                select count(*)
                from customer
                where email = ?
                """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);

        return count != null && count > 0;
    }

    @Override
    public boolean existsCustomerId(Integer id) {

        var sql = """
                select count(*)
                from customer
                where id = ?
                """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);

        return count != null && count > 0 ;
    }
}
