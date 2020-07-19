package com.airasia.booking.dto;

import com.airasia.booking.entities.Hotel;
import com.airasia.booking.entities.Room;
import com.airasia.booking.model.RoomRequestResponse;
import com.airasia.booking.repositories.HotelRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class RoomRequestMapper {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ModelMapper modelMapper;

    public RoomRequestMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.modelMapper.createTypeMap(RoomRequestResponse.class, Room.class)
                .addMappings(mapper -> {
                    mapper.skip(Room::setId);
                    mapper.using(ctx -> getHotel((String) ctx.getSource()))
                            .map(RoomRequestResponse::getHotelId, Room::setHotel);
                });

    }

    public Hotel getHotel(String id) {
        if (isInvalidId(id)) {
            return null;
        }
        return hotelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        MessageFormat.format("Invalid Hotel id: {0}", id)));
    }

    private static boolean isInvalidId(String id) {
        if (id == null) {
            return true;
        }

        return id.trim()
                .isEmpty();
    }


    public Room convertToEntity(RoomRequestResponse dto) {
        return modelMapper.map(dto, Room.class);
    }

    public RoomRequestResponse convertToDto(Room room) {
        return modelMapper.map(room, RoomRequestResponse.class);
    }

    public Room merge(RoomRequestResponse dto, Room entity) {
        modelMapper.map(dto, entity);
        return entity;
    }
}
