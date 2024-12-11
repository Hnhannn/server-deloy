package com.yuki.repositoty;

import com.yuki.entity.SliderShow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SliderShowDAO extends JpaRepository<SliderShow, Integer> {
    // Define custom queries if any
    // Example: Find all slider shows by current order
    List<SliderShow> findAllByOrderByCurrentAsc();
    @Query("SELECT MAX(s.current) FROM SliderShow s")
    Optional<Integer> findMaxCurrent();
}
