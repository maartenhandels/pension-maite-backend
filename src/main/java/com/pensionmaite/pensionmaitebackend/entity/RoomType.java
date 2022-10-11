package com.pensionmaite.pensionmaitebackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class RoomType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roomType_seq_generator")
    @SequenceGenerator(name = "roomType_seq_generator", sequenceName = "seq_roomType", allocationSize = 1)
    private Integer id;

    @Column(unique = true)
    private String name;

    private Integer capacity;

    @JsonIgnore
    @OneToMany(mappedBy="roomType")
    private Set<Room> rooms;

    public RoomType(String name, Integer capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "RoomType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                '}';
    }
}
