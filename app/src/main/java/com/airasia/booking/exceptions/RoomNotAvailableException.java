package com.airasia.booking.exceptions;

import lombok.Value;

@Value
public class RoomNotAvailableException extends RuntimeException {
    private static final long serialVersionUID = -5412287990019106366L;

    private String hotelId;
    private String roomId;


    public RoomNotAvailableException(String hotelId, String roomId) {
        this.hotelId = hotelId;
        this.roomId = roomId;

    }
}
