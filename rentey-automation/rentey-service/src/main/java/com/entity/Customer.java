package com.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity class representing the rental.Customers table.
 */
@Entity
@Table(name = "Customers", schema = "rental")
public class Customer {

    @Id
    @Column(name = "Id")
    private Integer id;

    @Column(name = "DisplayName", length = Integer.MAX_VALUE)
    private String displayName;

    @Column(name = "DateOfBirth")
    private LocalDateTime dateOfBirth;

    @Column(name = "NationalityId")
    private Integer nationalityId;

    @Column(name = "Email", length = Integer.MAX_VALUE)
    private String email;

    @Column(name = "GenderId")
    private Integer genderId;

    @Column(name = "PrimaryPhone", length = Integer.MAX_VALUE)
    private String primaryPhone;

    @Column(name = "OrganizationId", columnDefinition = "uniqueidentifier")
    private UUID organizationId;

    @Column(name = "TenantId")
    private Integer tenantId;

    @Column(name = "RowVersion", columnDefinition = "timestamp")
    private byte[] rowVersion;

    @Column(name = "VIPLevelId")
    private Long vipLevelId;

    @Column(name = "Guid", columnDefinition = "uniqueidentifier")
    private UUID guid;

    @Column(name = "LiteCustomerId")
    private Integer liteCustomerId;

    @Column(name = "OccupationId")
    private Integer occupationId;

    @Column(name = "DataSharingPolicyDate")
    private LocalDateTime dataSharingPolicyDate;

    @Column(name = "MarketingMaterialsDate")
    private LocalDateTime marketingMaterialsDate;

    @Column(name = "TermsConditionsAndPrivacyPolicyDate")
    private LocalDateTime termsConditionsAndPrivacyPolicyDate;

    // Default constructor
    public Customer() {
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public LocalDateTime getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDateTime dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getNationalityId() {
        return nationalityId;
    }

    public void setNationalityId(Integer nationalityId) {
        this.nationalityId = nationalityId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getGenderId() {
        return genderId;
    }

    public void setGenderId(Integer genderId) {
        this.genderId = genderId;
    }

    public String getPrimaryPhone() {
        return primaryPhone;
    }

    public void setPrimaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(UUID organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public byte[] getRowVersion() {
        return rowVersion;
    }

    public void setRowVersion(byte[] rowVersion) {
        this.rowVersion = rowVersion;
    }

    public Long getVipLevelId() {
        return vipLevelId;
    }

    public void setVipLevelId(Long vipLevelId) {
        this.vipLevelId = vipLevelId;
    }

    public UUID getGuid() {
        return guid;
    }

    public void setGuid(UUID guid) {
        this.guid = guid;
    }

    public Integer getLiteCustomerId() {
        return liteCustomerId;
    }

    public void setLiteCustomerId(Integer liteCustomerId) {
        this.liteCustomerId = liteCustomerId;
    }

    public Integer getOccupationId() {
        return occupationId;
    }

    public void setOccupationId(Integer occupationId) {
        this.occupationId = occupationId;
    }

    public LocalDateTime getDataSharingPolicyDate() {
        return dataSharingPolicyDate;
    }

    public void setDataSharingPolicyDate(LocalDateTime dataSharingPolicyDate) {
        this.dataSharingPolicyDate = dataSharingPolicyDate;
    }

    public LocalDateTime getMarketingMaterialsDate() {
        return marketingMaterialsDate;
    }

    public void setMarketingMaterialsDate(LocalDateTime marketingMaterialsDate) {
        this.marketingMaterialsDate = marketingMaterialsDate;
    }

    public LocalDateTime getTermsConditionsAndPrivacyPolicyDate() {
        return termsConditionsAndPrivacyPolicyDate;
    }

    public void setTermsConditionsAndPrivacyPolicyDate(LocalDateTime termsConditionsAndPrivacyPolicyDate) {
        this.termsConditionsAndPrivacyPolicyDate = termsConditionsAndPrivacyPolicyDate;
    }
}

