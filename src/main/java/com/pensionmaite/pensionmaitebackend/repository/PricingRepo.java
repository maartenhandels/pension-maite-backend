package com.pensionmaite.pensionmaitebackend.repository;

import com.pensionmaite.pensionmaitebackend.entity.Pricing;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PricingRepo extends CrudRepository<Pricing, Long> {

    @Query("SELECT p FROM Pricing p JOIN RoomType rt ON p.roomType.id = rt.id WHERE rt.name = :roomTypeName")
    List<Pricing> findByRoomTypeName(String roomTypeName);

    @Query("SELECT p FROM Pricing p " +
            "JOIN RoomType rt ON p.roomType.id = rt.id " +
            "WHERE rt.name = :roomTypeName " +
            "AND ((" +
            "   p.startDate <= :endDate " +
            "   AND (p.endDate IS NULL OR p.endDate >= :startDate)) " +
            "OR (" +
            "   :startDate <= p.endDate " +
            "   AND :endDate >= p.startDate" +
            "))")
    List<Pricing> findOverlappingPricingsForRoomType(String roomTypeName, LocalDate startDate, LocalDate endDate);

    @Query("SELECT p FROM Pricing p " +
            "JOIN RoomType rt ON p.roomType.id = rt.id " +
            "WHERE rt.name = :roomTypeName " +
            "AND p.startDate IS NULL " +
            "AND p.endDate IS NULL ")
    Optional<Pricing> findDefaultPricingForRoomType(String roomTypeName);
}
