package com.pensionmaite.pensionmaitebackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@Data
@Entity
@Table(name = "pricing")
public class Pricing {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pricing_seq_generator")
    @SequenceGenerator(name = "pricing_seq_generator", sequenceName = "seq_pricing", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_type_id")
    private RoomType roomType;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "price")
    private BigDecimal price;

    public Pricing(RoomType roomType, BigDecimal price) {
        this.roomType = roomType;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Pricing{" +
                "id=" + id +
                ", roomType=" + roomType.getName() +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", price=" + price +
                '}';
    }
}
