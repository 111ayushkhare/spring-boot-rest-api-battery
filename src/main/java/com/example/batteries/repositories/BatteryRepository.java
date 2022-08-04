package com.example.batteries.repositories;

import com.example.batteries.entities.Battery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatteryRepository extends JpaRepository<Battery, Long> {

    @Query("FROM Battery WHERE postcode >=:postcodeLow AND postcode <=:postcodeHigh")
    List<Battery> findAllBatteriesWithinSpecifiedPostcodeRange(int postcodeLow, int postcodeHigh);
}
