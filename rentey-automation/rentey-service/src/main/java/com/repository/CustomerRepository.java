package com.repository;

import com.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for accessing rental.Customers table.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    /**
     * Find all customers.
     *
     * @return List of all customers
     */
    @Override
    @NonNull
    List<Customer> findAll();

    /**
     * Find customer by ID.
     *
     * @param id Customer ID
     * @return Optional Customer
     */
    @Override
    @NonNull
    Optional<Customer> findById(@NonNull Integer id);

    /**
     * Find customers by email.
     *
     * @param email Customer email
     * @return List of customers with the given email
     */
    List<Customer> findByEmail(String email);

    /**
     * Find customers by primary phone.
     *
     * @param primaryPhone Customer primary phone
     * @return List of customers with the given phone
     */
    List<Customer> findByPrimaryPhone(String primaryPhone);

    /**
     * Find customers by tenant ID.
     *
     * @param tenantId Tenant ID
     * @return List of customers for the given tenant
     */
    List<Customer> findByTenantId(Integer tenantId);

    /**
     * Find customer by GUID.
     *
     * @param guid Customer GUID
     * @return Optional Customer
     */
    Optional<Customer> findByGuid(UUID guid);

    /**
     * Find customers by organization ID.
     *
     * @param organizationId Organization ID
     * @return List of customers for the given organization
     */
    List<Customer> findByOrganizationId(UUID organizationId);

    /**
     * Find customers by nationality ID.
     *
     * @param nationalityId Nationality ID
     * @return List of customers with the given nationality
     */
    List<Customer> findByNationalityId(Integer nationalityId);

    /**
     * Find customers by gender ID.
     *
     * @param genderId Gender ID
     * @return List of customers with the given gender
     */
    List<Customer> findByGenderId(Integer genderId);
}

