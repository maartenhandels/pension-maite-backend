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

@Log4j2
@Service
public class RoomTypeServiceImpl implements RoomTypeService {

    @Autowired
    RoomTypeRepo roomTypeRepo;

    @Override
    public CreateRoomTypeResponse createRoomType(CreateRoomTypeRequest request) throws UniqueConstraintException {

        log.debug("CreateRoomTypeRequest: ", request);

        RoomType roomType = roomTypeRepo.findByName(request.getName());

        if (roomType != null) {
            throw new UniqueConstraintException("RoomType name must be unique");
        }

        roomType = new RoomType(request.getName().toUpperCase(), request.getCapacity());
        roomType = roomTypeRepo.save(roomType);

        return new CreateRoomTypeResponse(roomType);
    }
}
