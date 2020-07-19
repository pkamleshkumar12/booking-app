package com.airasia.booking.entities;

import com.airasia.booking.enums.RoomType;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Data
@Table(name="rooms")
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "hotel" })
public class Room  extends AuditEntity implements Serializable {
    private static final long serialVersionUID = -5447023441800227916L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @NotNull
    @Column(name="room_number", unique = true, length = 4)
    private String roomNumber;

    @NotNull
    @Column(name = "type", length = 30)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private RoomType roomType =  RoomType.PREMIER;

    @Digits(integer = 2, fraction = 0)
    @Column(name = "occupancy", nullable = false, length = 2)
    private Integer occupancy;

    @Digits(integer = 2, fraction = 0)
    @Column(name = "beds", nullable = false, length = 2)
    @Builder.Default
    private Integer beds = 1;

    @NotNull
    @Digits(integer = 5, fraction = 2)
    @Column(name = "amount",  precision = 6, scale = 2)
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name="hotel_id", nullable = false, columnDefinition="char(36)")
    private Hotel hotel;



}
