package com.builders;

import com.beans.booking.CreateBookingRequestBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder class for creating CreateBookingRequestBean with default values.
 * Allows customization of any field using fluent builder methods.
 */
public class CreateBookingRequestBuilder {

    // Booking basic information
    private Integer countryId = null;
    private String pickupDate = null;
    private String dropoffDate = null;
    private Integer pickupBranchId = null;
    private Integer dropoffBranchId = null;
    private String categoryId = null;
    private String modelId = null;
    private Integer year = null;
    private Integer driverId = null;
    private Integer rentalRateId = null;
    private Integer transferCostId = null;
    private Integer sourceId = null;

    // Lists
    private List<CreateBookingRequestBean.VoucherCreateInput> voucherCreateInputList = new ArrayList<>();
    private List<Object> extras = new ArrayList<>();
    private String couponCode = null;
    private List<Object> userDiscounts = new ArrayList<>();
    private List<Object> bookingOffers = new ArrayList<>();

    /**
     * Creates a new CreateBookingRequestBuilder with default values.
     */
    public CreateBookingRequestBuilder() {
    }

    /**
     * Creates a new CreateBookingRequestBuilder with default values.
     * This is a factory method that creates a new builder instance.
     *
     * @return new CreateBookingRequestBuilder instance
     */
    public static CreateBookingRequestBuilder create() {
        return new CreateBookingRequestBuilder();
    }

    // Builder methods for basic booking information
    public CreateBookingRequestBuilder withCountryId(Integer countryId) {
        this.countryId = countryId;
        return this;
    }

    public CreateBookingRequestBuilder withPickupDate(String pickupDate) {
        this.pickupDate = pickupDate;
        return this;
    }

    public CreateBookingRequestBuilder withDropoffDate(String dropoffDate) {
        this.dropoffDate = dropoffDate;
        return this;
    }

    public CreateBookingRequestBuilder withPickupBranchId(Integer pickupBranchId) {
        this.pickupBranchId = pickupBranchId;
        return this;
    }

    public CreateBookingRequestBuilder withDropoffBranchId(Integer dropoffBranchId) {
        this.dropoffBranchId = dropoffBranchId;
        return this;
    }

    public CreateBookingRequestBuilder withCategoryId(String categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public CreateBookingRequestBuilder withModelId(String modelId) {
        this.modelId = modelId;
        return this;
    }

    public CreateBookingRequestBuilder withYear(Integer year) {
        this.year = year;
        return this;
    }

    public CreateBookingRequestBuilder withDriverId(Integer driverId) {
        this.driverId = driverId;
        return this;
    }

    public CreateBookingRequestBuilder withRentalRateId(Integer rentalRateId) {
        this.rentalRateId = rentalRateId;
        return this;
    }

    public CreateBookingRequestBuilder withTransferCostId(Integer transferCostId) {
        this.transferCostId = transferCostId;
        return this;
    }

    public CreateBookingRequestBuilder withSourceId(Integer sourceId) {
        this.sourceId = sourceId;
        return this;
    }

    public CreateBookingRequestBuilder withBookingDetails(
            Integer countryId,
            String pickupDate,
            String dropoffDate,
            Integer pickupBranchId,
            Integer dropoffBranchId,
            String categoryId,
            String modelId,
            Integer year,
            Integer driverId,
            Integer rentalRateId,
            Integer transferCostId,
            Integer sourceId) {
        this.countryId = countryId;
        this.pickupDate = pickupDate;
        this.dropoffDate = dropoffDate;
        this.pickupBranchId = pickupBranchId;
        this.dropoffBranchId = dropoffBranchId;
        this.categoryId = categoryId;
        this.modelId = modelId;
        this.year = year;
        this.driverId = driverId;
        this.rentalRateId = rentalRateId;
        this.transferCostId = transferCostId;
        this.sourceId = sourceId;
        return this;
    }

    // Builder methods for VoucherCreateInput
    public CreateBookingRequestBuilder withVoucherCreateInput(
            Integer voucherOperationTypeId,
            Integer voucherTypeId,
            CreateBookingRequestBean.BasePaymentInformationDto basePaymentInformationDto,
            Integer sourceId) {
        CreateBookingRequestBean.VoucherCreateInput voucher = new CreateBookingRequestBean.VoucherCreateInput(
                voucherOperationTypeId,
                voucherTypeId,
                basePaymentInformationDto,
                sourceId
        );
        this.voucherCreateInputList.add(voucher);
        return this;
    }

    public CreateBookingRequestBuilder withVoucherCreateInput(CreateBookingRequestBean.VoucherCreateInput voucher) {
        this.voucherCreateInputList.add(voucher);
        return this;
    }

    public CreateBookingRequestBuilder withVoucherCreateInputList(List<CreateBookingRequestBean.VoucherCreateInput> voucherCreateInputList) {
        this.voucherCreateInputList = voucherCreateInputList != null ? new ArrayList<>(voucherCreateInputList) : new ArrayList<>();
        return this;
    }

    public CreateBookingRequestBuilder clearVoucherCreateInputList() {
        this.voucherCreateInputList.clear();
        return this;
    }

    // Helper method to create BasePaymentInformationDto
    public CreateBookingRequestBean.BasePaymentInformationDto createBasePaymentInformationDto(
            String discriminator,
            Integer paymentMethodId,
            CreateBookingRequestBean.Amount amount) {
        return new CreateBookingRequestBean.BasePaymentInformationDto(
                discriminator,
                paymentMethodId,
                amount
        );
    }

    // Helper method to create Amount
    public CreateBookingRequestBean.Amount createAmount(Integer value, Integer currencyId) {
        return new CreateBookingRequestBean.Amount(value, currencyId);
    }

    // Builder methods for Extras
    public CreateBookingRequestBuilder withExtra(Object extra) {
        this.extras.add(extra);
        return this;
    }

    public CreateBookingRequestBuilder withExtras(List<Object> extras) {
        this.extras = extras != null ? new ArrayList<>(extras) : new ArrayList<>();
        return this;
    }

    public CreateBookingRequestBuilder clearExtras() {
        this.extras.clear();
        return this;
    }

    // Builder methods for Coupon Code
    public CreateBookingRequestBuilder withCouponCode(String couponCode) {
        this.couponCode = couponCode;
        return this;
    }

    // Builder methods for User Discounts
    public CreateBookingRequestBuilder withUserDiscount(Object userDiscount) {
        this.userDiscounts.add(userDiscount);
        return this;
    }

    public CreateBookingRequestBuilder withUserDiscounts(List<Object> userDiscounts) {
        this.userDiscounts = userDiscounts != null ? new ArrayList<>(userDiscounts) : new ArrayList<>();
        return this;
    }

    public CreateBookingRequestBuilder clearUserDiscounts() {
        this.userDiscounts.clear();
        return this;
    }

    // Builder methods for Booking Offers
    public CreateBookingRequestBuilder withBookingOffer(Object bookingOffer) {
        this.bookingOffers.add(bookingOffer);
        return this;
    }

    public CreateBookingRequestBuilder withBookingOffers(List<Object> bookingOffers) {
        this.bookingOffers = bookingOffers != null ? new ArrayList<>(bookingOffers) : new ArrayList<>();
        return this;
    }

    public CreateBookingRequestBuilder clearBookingOffers() {
        this.bookingOffers.clear();
        return this;
    }

    /**
     * Builds and returns the CreateBookingRequestBean with the configured values.
     *
     * @return CreateBookingRequestBean instance
     */
    public CreateBookingRequestBean build() {
        return new CreateBookingRequestBean(
                countryId,
                pickupDate,
                dropoffDate,
                pickupBranchId,
                dropoffBranchId,
                categoryId,
                modelId,
                year,
                driverId,
                rentalRateId,
                transferCostId,
                sourceId,
                voucherCreateInputList.isEmpty() ? null : voucherCreateInputList,
                extras.isEmpty() ? null : extras,
                couponCode,
                userDiscounts.isEmpty() ? null : userDiscounts,
                bookingOffers.isEmpty() ? null : bookingOffers
        );
    }
}
