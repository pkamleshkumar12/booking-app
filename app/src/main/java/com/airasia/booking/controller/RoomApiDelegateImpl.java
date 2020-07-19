package com.airasia.booking.controller;

import com.airasia.booking.api.RoomsApiDelegate;
import com.airasia.booking.dto.RoomRequestMapper;
import com.airasia.booking.model.RoomListResponse;
import com.airasia.booking.model.RoomRequestResponse;
import com.airasia.booking.repositories.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
@Slf4j
@Service
public class RoomApiDelegateImpl implements RoomsApiDelegate {

    @Autowired
    private RoomRequestMapper roomRequestMapper;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<RoomListResponse> getHotelsHotelIdRooms(String hotelId, Pageable pageable){

        val rooms = roomRepository.findAllByHotelId(hotelId, pageable);
        Page<RoomRequestResponse> response = rooms.map(roomRequestMapper::convertToDto);
        return ResponseEntity.ok(modelMapper.map(response, RoomListResponse.class));
    }

    @Override
    public ResponseEntity<RoomRequestResponse> getHotelsHotelIdRoomsRoomId(String roomId,
                                                                           String contentType) {

        val roomRequestResult = roomRepository.findById(roomId);
        return roomRequestResult
                .map(r -> ResponseEntity.ok(roomRequestMapper.convertToDto(r)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @Override
    public ResponseEntity<RoomRequestResponse> postHotelsHotelIdRooms(String contentType,
                                                                      RoomRequestResponse roomRequestResponse) {

        val room = roomRequestMapper.convertToEntity(roomRequestResponse);
        val updatedRoom = roomRepository.save(room);
        val response = roomRequestMapper.convertToDto(updatedRoom);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<RoomRequestResponse> putHotelsHotelIdRoomsRoomId(String roomId,
                                                                           String contentType,
                                                                           RoomRequestResponse roomRequestResponse) {

        val roomRequestResult = roomRepository.findById(roomId);

        ResponseEntity<RoomRequestResponse> response = roomRequestResult
                .map(room -> {
                    roomRequestMapper.merge(roomRequestResponse, room);
                    val updatedRoom = roomRepository.save(room);
                    val body = roomRequestMapper.convertToDto(updatedRoom);
                    return ResponseEntity.ok(body);
                }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));

        return response;
    }

    @Override
    public ResponseEntity<Object> deleteHotelsHotelIdRoomsRoomId(String roomId,
                                                               String contentType) {

        return roomRepository.findById(roomId)
                .map(room -> {
                    room.setActive(false);
                    room.setDeleted(true);
                    roomRepository.save(room);
                    return ResponseEntity.ok()
                            .build();
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }
}
