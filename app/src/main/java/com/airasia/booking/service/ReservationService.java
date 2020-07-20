package com.airasia.booking.service;

import com.airasia.booking.entities.Customer;
import com.airasia.booking.entities.Reservation;
import com.airasia.booking.entities.Room;
import com.airasia.booking.model.ReservationRequestResponse;
import com.airasia.booking.repositories.ReservationRepository;
import com.airasia.booking.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {


    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    public Optional<Room> findAllAvailableRooms(ReservationRequestResponse request){

        return roomRepository.findAvailableRooms(request.getHotelId(), request.getCheckInDate(), request.getCheckOutDate());
    }

    public Reservation reserveRoom(ReservationRequestResponse request, Room room){

        Customer customer = Customer.builder()
                .customerFullName(request.getCustomer().getName())
                .customerEmail(request.getCustomer().getEmail())
                .customerPhoneNumber(request.getCustomer().getPhoneNumber())
                .build();

        Reservation reservation = Reservation.builder()
                .room(room)
                .hotel(room.getHotel())
                .checkInDate(LocalDate.parse(request.getCheckInDate()))
                .checkOutDate(LocalDate.parse(request.getCheckInDate()))
                .roomNumber(room.getRoomNumber())
                .customer(customer)
                .build();

        return reservationRepository.save(reservation);
    }
}
