package com.airasia.booking.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Entity
@Data
@Table(name="hotels")
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Where(clause="deleted=0")
public class Hotel extends AuditEntity implements Serializable {
    private static final long serialVersionUID = -5383522349486604728L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @NotNull
    @Column(name = "name", length = 50, unique = true)
    private String name;

    @NotNull
    @Column(name = "location", length = 50)
    private String location;

}
