package com.airasia.booking.repositories;


import com.airasia.booking.entities.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, String>, QuerydslPredicateExecutor<Room> {

    Page<Room> findAllByHotelId(String hotelId, Pageable pageable);


    @Query(
            value = "SELECT * FROM ROOMS AS r\n" +
                    "LEFT JOIN RESERVATIONS rs\n" +
                    "ON r.HOTEL_ID = rs.HOTEL_ID\n" +
                    "AND r.ID = rs.ROOM_ID\n" +
                    "AND rs.check_in_date >= :checkInDate\n" +
                    "AND rs.check_out_date <= :checkOutDate\n" +
                    "WHERE\n" +
                    "r.hotel_id = :hotelId\n" +
                    "AND rs.id IS NULL\n" +
                    "LIMIT 1",
            nativeQuery = true)
    Optional<Room> findAvailableRooms(@Param("hotelId")String hotelId,
                                      @Param("checkInDate")String checkInDate,
                                      @Param("checkOutDate")String checkOutDate);
}
