package com.airasia.booking.controller;

import com.airasia.booking.api.ReservationsApiDelegate;
import com.airasia.booking.dto.ReservationRequestMapper;
import com.airasia.booking.entities.Room;
import com.airasia.booking.exceptions.RoomNotAvailableException;
import com.airasia.booking.model.ReservationRequestResponse;
import com.airasia.booking.service.ReservationService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class ReservationsApiDelegateImpl implements ReservationsApiDelegate {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRequestMapper reservationRequestMapper;

    public ResponseEntity<ReservationRequestResponse> postReservations(String contentType,
                                                                ReservationRequestResponse reservationRequestResponse) {


        Optional<Room> availableRoom = reservationService.findAllAvailableRooms(reservationRequestResponse);
        if (availableRoom.isPresent()) {
            val reservation = reservationService.reserveRoom(reservationRequestResponse, availableRoom.get());
            val response = reservationRequestMapper.convertToDto(reservation);
            return ResponseEntity.ok(response);

        } else {
            throw new RoomNotAvailableException(reservationRequestResponse.getHotelId(), reservationRequestResponse.getRoom().getId());
        }
    }
}
