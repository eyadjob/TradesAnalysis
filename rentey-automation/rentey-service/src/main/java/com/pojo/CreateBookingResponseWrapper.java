package com.pojo;

import com.beans.booking.CreateBookingRequestBean;
import com.beans.booking.CreateBookingResponseBean;
import com.beans.customer.CreateOrUpdateCustomerResponseBean;

public class CreateBookingResponseWrapper {
    public CreateBookingResponseBean createBookingResponseBean;
    public CreateBookingRequestBean createBookingRequestBean;
    public CreateOrUpdateCustomerResponseBean customerResponseBean;

    public CreateBookingResponseWrapper(CreateBookingResponseBean createBookingResponseBean, CreateBookingRequestBean createBookingRequestBean) {
        this.createBookingResponseBean = createBookingResponseBean;
        this.createBookingRequestBean = createBookingRequestBean;
    }

    public CreateBookingResponseWrapper(CreateBookingResponseBean createBookingResponseBean, CreateBookingRequestBean createBookingRequestBean, CreateOrUpdateCustomerResponseBean customerResponseBean) {
        this.createBookingResponseBean = createBookingResponseBean;
        this.createBookingRequestBean = createBookingRequestBean;
        this.customerResponseBean = customerResponseBean;
    }

    public CreateOrUpdateCustomerResponseBean getCustomerResponseBean() {
        return customerResponseBean;
    }

    public void setCustomerResponseBean(CreateOrUpdateCustomerResponseBean customerResponseBean) {
        this.customerResponseBean = customerResponseBean;
    }

    public CreateBookingResponseBean getCreateBookingResponseBean() {
        return createBookingResponseBean;
    }

    public void setCreateBookingResponseBean(CreateBookingResponseBean createBookingResponseBean) {
        this.createBookingResponseBean = createBookingResponseBean;
    }

    public CreateBookingRequestBean getCreateBookingRequestBean() {
        return createBookingRequestBean;
    }

    public void setCreateBookingRequestBean(CreateBookingRequestBean createBookingRequestBean) {
        this.createBookingRequestBean = createBookingRequestBean;
    }


}
