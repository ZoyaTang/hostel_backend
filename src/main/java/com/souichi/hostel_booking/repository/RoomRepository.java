package com.souichi.hostel_booking.repository;

import com.souichi.hostel_booking.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
