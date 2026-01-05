package com.pojo;

import com.beans.booking.CreateBookingRequestBean;
import com.beans.booking.CreateBookingResponseBean;

public class CreateBookingResponseWrapper {
    public CreateBookingResponseBean createBookingResponseBean;
    public CreateBookingRequestBean createBookingRequestBean;

    public CreateBookingResponseWrapper(CreateBookingResponseBean createBookingResponseBean, CreateBookingRequestBean createBookingRequestBean) {
        this.createBookingResponseBean = createBookingResponseBean;
        this.createBookingRequestBean = createBookingRequestBean;
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
