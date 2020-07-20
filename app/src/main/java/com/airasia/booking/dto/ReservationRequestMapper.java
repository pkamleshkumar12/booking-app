package com.airasia.booking.dto;

import com.airasia.booking.entities.Reservation;
import com.airasia.booking.model.ReservationRequestResponse;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReservationRequestMapper {

    @Autowired
    private ModelMapper modelMapper;

    public ReservationRequestMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
        this.modelMapper.createTypeMap(ReservationRequestResponse.class, Reservation.class)
                .addMappings(mapper -> mapper.skip(Reservation::setId));
    }


    public ReservationRequestResponse convertToDto(Reservation reservation){


        val dto = modelMapper.map(reservation, ReservationRequestResponse.class);
        return dto;
    }
}
