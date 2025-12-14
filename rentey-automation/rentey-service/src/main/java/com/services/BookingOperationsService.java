package com.services;

import com.beans.booking.CreateBookingResponseBean;
import com.beans.general.AbpResponseBean;
import com.beans.vehicle.CreateVehiclesResponseBean;
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


    public CreateBookingResponseBean createNewBooking(String countryName, String branchName) {
        CreateVehiclesResponseBean createResponse = vehicleOperationsService.createVehicleWithRandomPlateNumber(countryName, branchName);
        vehicleOperationsService.createAndReceiveNewVehicle(countryName, branchName,createResponse);






    }
}
