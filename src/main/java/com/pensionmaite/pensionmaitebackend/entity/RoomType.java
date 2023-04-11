package com.pensionmaite.pensionmaitebackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
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
    @OneToMany(mappedBy="roomType")
    private Set<Room> rooms;

    @JsonIgnore
    @OneToMany(mappedBy = "roomType")
    private List<Pricing> pricingList = new ArrayList<>();

    public RoomType(String name, Integer capacity, String imageFilename) {
        this.name = name;
        this.capacity = capacity;
        this.imageFilename = imageFilename;
    }

    @Override
    public String toString() {
        return "RoomType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                ", imageUrl='" + imageFilename + '\'' +
                '}';
    }
}
