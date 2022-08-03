package com.example.batteries.repositories;

import com.example.batteries.entities.Battery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BatteryRepository extends CrudRepository<Battery, Long> {

    @Query("FROM Battery WHERE postcode >=:postcodeLow AND postcode <=:postcodeHigh")
    List<Battery> findAllBatteriesWithinSpecifiedPostcodeRange(int postcodeLow, int postcodeHigh);
}
