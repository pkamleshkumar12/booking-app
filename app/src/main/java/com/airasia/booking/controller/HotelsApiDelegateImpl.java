package com.airasia.booking.controller;

import com.airasia.booking.api.HotelsApiDelegate;
import com.airasia.booking.dto.HotelRequestMapper;
import com.airasia.booking.model.HotelListResponse;
import com.airasia.booking.model.HotelRequestResponse;
import com.airasia.booking.repositories.HotelRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HotelsApiDelegateImpl implements HotelsApiDelegate {

    @Autowired
    private HotelRequestMapper hotelRequestMapper;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<HotelListResponse> getHotels(Pageable pageable) {

        val hotels = hotelRepository.findAll(pageable);
        Page<HotelRequestResponse> response = hotels.map(hotelRequestMapper::convertToDto);
        return ResponseEntity.ok(modelMapper.map(response, HotelListResponse.class));
    }

    @Override
    public ResponseEntity<HotelRequestResponse> getHotelsHotelId(String hotelId,
                                                                 String contentType) {

        val hotelRequestResult = hotelRepository.findById(hotelId);
        return hotelRequestResult
                .map(r -> ResponseEntity.ok(hotelRequestMapper.convertToDto(r)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @Override
    public ResponseEntity<HotelRequestResponse> postHotels(String contentType, HotelRequestResponse hotelRequestResponse) {

        val hotel = hotelRequestMapper.convertToEntity(hotelRequestResponse);
        val updatedHotel = hotelRepository.save(hotel);
        val response = hotelRequestMapper.convertToDto(updatedHotel);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<HotelRequestResponse> putHotelsHotelId(String hotelId,
                                                                 String contentType,
                                                                 HotelRequestResponse hotelRequestResponse) {

        val hotelRequestResult = hotelRepository.findById(hotelId);
        ResponseEntity<HotelRequestResponse> response = hotelRequestResult
                .map(r -> {
                    hotelRequestMapper.merge(hotelRequestResponse, r);
                    val updatedHotel = hotelRepository.save(r);
                    val body = hotelRequestMapper.convertToDto(updatedHotel);
                    return ResponseEntity.ok(body);
                }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));

        return response;
    }

    @Override
    public ResponseEntity<Object> deleteHotelsHotelId(String hotelId, String contentType) {

        return hotelRepository.findById(hotelId)
                .map(hotel -> {
                    hotel.setActive(false);
                    hotel.setDeleted(true);
                    hotelRepository.save(hotel);
                    return ResponseEntity.ok()
                            .build();
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));

    }
}

