package com.pensionmaite.pensionmaitebackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "room_type")
public class RoomType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roomType_seq_generator")
    @SequenceGenerator(name = "roomType_seq_generator", sequenceName = "seq_room_type", allocationSize = 1)
    private Integer id;

    @Column(unique = true)
    private String name;

    private Integer capacity;

    private String imageFilename;

    @JsonIgnore
    @OneToMany(mappedBy = "roomType")
    private List<Pricing> pricingList = new ArrayList<>();

    public RoomType(String name, Integer capacity, String imageFilename) {
        this.name = name;
        this.capacity = capacity;
        this.imageFilename = imageFilename;
    }
}
