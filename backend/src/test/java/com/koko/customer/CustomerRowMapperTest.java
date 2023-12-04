package com.koko.customer;

import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

class CustomerRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        // Given
        CustomerRowMapper underTest;
        int rowNum = 1;
        ResultSet resultSet = mock(ResultSet.class);
        underTest = new CustomerRowMapper();
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("koko");
        when(resultSet.getString("email")).thenReturn("koko@example.com");
        when(resultSet.getInt("age")).thenReturn(54);
        when(resultSet.getString("gender")).thenReturn(Gender.MALE.toString());

        // When
        Customer actualCustomer = underTest.mapRow(resultSet, rowNum);
        Customer expectedCustomer = new Customer(
                rowNum, "koko", "koko@example.com", "password", Gender.MALE, 54
        );
        // Then
        assertThat(actualCustomer).isEqualTo(expectedCustomer);
    }
}