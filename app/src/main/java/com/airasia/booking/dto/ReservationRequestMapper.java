package com.airasia.booking.dto;

import com.airasia.booking.entities.Customer;
import com.airasia.booking.entities.Reservation;
import com.airasia.booking.model.CustomerDetails;
import com.airasia.booking.model.ReservationRequestResponse;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReservationRequestMapper {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RoomRequestMapper roomRequestMapper;

    public ReservationRequestMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
        this.modelMapper.createTypeMap(ReservationRequestResponse.class, Reservation.class)
                .addMappings(mapper -> mapper.skip(Reservation::setId));
    }


    public ReservationRequestResponse convertToDto(Reservation reservation){

        val customerDetails = new CustomerDetails()
                .email(reservation.getCustomer().getCustomerEmail())
                .name(reservation.getCustomer().getCustomerFullName())
                .phoneNumber(reservation.getCustomer().getCustomerPhoneNumber());

        val room =roomRequestMapper.convertToDto(reservation.getRoom());

        val dto = modelMapper.map(reservation, ReservationRequestResponse.class);
        dto.setHotelId(reservation.getHotel().getId());
        dto.setHotelName(reservation.getHotel().getName());
        dto.setCustomer(customerDetails);
        dto.setRoom(room);

        return dto;
    }
}
