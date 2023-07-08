package com.pensionmaite.pensionmaitebackend.events.dto;

import com.pensionmaite.pensionmaitebackend.entity.Room;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class ReservedRoomType {


    private String roomTypeName;

    private Integer numberOfRooms;

    private String imageFilename;

    public static List<ReservedRoomType> parseRoomListToReservedRoomTypeList(Set<Room> rooms) {
        Map<String, ReservedRoomType> reservedRoomTypeMap = new HashMap<>();
        for (Room room:rooms) {
            String roomTypeName = room.getRoomType().getName();
            if (reservedRoomTypeMap.containsKey(roomTypeName)) {
                ReservedRoomType reservedRoomType = reservedRoomTypeMap.get(roomTypeName);
                reservedRoomType.setNumberOfRooms(reservedRoomType.getNumberOfRooms() + 1);
                reservedRoomTypeMap.put(roomTypeName, reservedRoomType);
            } else {
                ReservedRoomType reservedRoomType = new ReservedRoomType();
                reservedRoomType.setRoomTypeName(roomTypeName);
                reservedRoomType.setNumberOfRooms(1);
                reservedRoomType.setImageFilename(room.getRoomType().getImageFilename());
                reservedRoomTypeMap.put(roomTypeName, reservedRoomType);
            }
        }
        return reservedRoomTypeMap.values().stream().collect(Collectors.toList());
    }
}
