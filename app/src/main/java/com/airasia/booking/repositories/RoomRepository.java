package com.airasia.booking.repositories;


import com.airasia.booking.entities.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface RoomRepository extends JpaRepository<Room, String>, QuerydslPredicateExecutor<Room> {

    Page<Room> findAllByHotelId(String hotelId, Pageable pageable);
}
