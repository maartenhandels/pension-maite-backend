package com.pensionmaite.pensionmaitebackend.service.impl;

import com.pensionmaite.pensionmaitebackend.entity.RoomType;
import com.pensionmaite.pensionmaitebackend.events.request.CreateRoomTypeRequest;
import com.pensionmaite.pensionmaitebackend.events.response.CreateRoomTypeResponse;
import com.pensionmaite.pensionmaitebackend.exception.UniqueConstraintException;
import com.pensionmaite.pensionmaitebackend.repository.RoomTypeRepo;
import com.pensionmaite.pensionmaitebackend.service.RoomTypeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
public class RoomTypeServiceImpl implements RoomTypeService {

    @Autowired
    RoomTypeRepo roomTypeRepo;

    @Override
    public CreateRoomTypeResponse createRoomType(CreateRoomTypeRequest request) throws UniqueConstraintException {

        log.debug("CreateRoomTypeRequest: {}", request);

        // Check if room type with the provided name exists
        if (roomTypeRepo.findByName(request.getName()).isPresent()) {
            throw new UniqueConstraintException("RoomType name must be unique");
        }

        // Create a new room type in the db if name is unique
        RoomType roomType = new RoomType(request.getName().toUpperCase(),
                request.getCapacity(),
                request.getImageFilename());
        roomType = roomTypeRepo.save(roomType);

        return new CreateRoomTypeResponse(roomType);
    }

    @Override
    public Optional<RoomType> getRoomTypeByName(String roomType) {
        return roomTypeRepo.findByName(roomType);
    }
}
