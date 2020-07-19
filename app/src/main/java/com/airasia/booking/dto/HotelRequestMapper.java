package com.airasia.booking.dto;

import com.airasia.booking.entities.Hotel;
import com.airasia.booking.model.HotelRequestResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class HotelRequestMapper {

    @Autowired
    private ModelMapper modelMapper;

    public HotelRequestMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.modelMapper.createTypeMap(HotelRequestResponse.class, Hotel.class)
                .addMappings(mapper -> {
                    mapper.skip(Hotel::setId);
                });
    }

    public Hotel convertToEntity(HotelRequestResponse dto) {
        return modelMapper.map(dto, Hotel.class);
    }

    public HotelRequestResponse convertToDto(Hotel hotel) {
        return modelMapper.map(hotel, HotelRequestResponse.class);
    }

    public Hotel merge(HotelRequestResponse dto, Hotel entity) {

        modelMapper.map(dto, entity);
        return entity;
    }
}
