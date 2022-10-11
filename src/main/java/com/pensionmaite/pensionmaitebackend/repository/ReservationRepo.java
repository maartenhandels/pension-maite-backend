package com.pensionmaite.pensionmaitebackend.repository;

import com.pensionmaite.pensionmaitebackend.entity.Reservation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepo extends CrudRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE" +
            "(r.checkinDate >= ?1 and r.checkinDate <  ?2) or" +
            "(r.checkoutDate > ?1 and checkoutDate <=  ?2) or" +
            "(checkinDate <= ?1 and checkoutDate > ?1)")
    List<Reservation> findReservationsBetweenDates(LocalDate date1, LocalDate date2);
}
