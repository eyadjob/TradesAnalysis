package com.services;

import com.beans.booking.CreateBookingResponseBean;
import com.beans.customer.CreateOrUpdateCustomerRequestBean;
import com.beans.customer.CreateOrUpdateCustomerResponseBean;
import com.beans.general.AbpResponseBean;
import com.beans.vehicle.CreateVehiclesResponseBean;
import com.entity.Customer;
import org.checkerframework.checker.guieffect.qual.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingOperationsService {

    @Autowired
    BookingService bookingService;

    @Autowired
    LookupsService lookupsService;

    @Autowired
    CountryService countryService;

    @Autowired
    VehicleService vehicleService;

    @Autowired
    VehicleOperationsService vehicleOperationsService;

    @Autowired
    CustomerService customerService;

    @Autowired
    CustomerOperationsService customerOperationsService;


    public CreateBookingResponseBean createNewBooking(String countryName, String branchName) {
        CreateOrUpdateCustomerResponseBean createOrUpdateCustomerResponseBean = customerOperationsService.createCustomerWithRandomData("Saudi Arabia");
        CreateVehiclesResponseBean createResponse = vehicleOperationsService.createVehicleWithRandomPlateNumber(countryName, branchName);
        vehicleOperationsService.createAndReceiveNewVehicle(countryName, branchName,createResponse);

    }
}
