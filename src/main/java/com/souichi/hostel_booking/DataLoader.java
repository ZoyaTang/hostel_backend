package com.souichi.hostel_booking;

import com.souichi.hostel_booking.model.Room;
import com.souichi.hostel_booking.repository.RoomRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final RoomRepository roomRepository;

    public DataLoader(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public void run(String... args) {
        if (roomRepository.count() == 0) {
            Room room1 = new Room();
            room1.setName("Standard Room");
            room1.setPricePerNight(5000);
            room1.setAvailable(true);
            roomRepository.save(room1);

            Room room2 = new Room();
            room2.setName("Deluxe Room");
            room2.setPricePerNight(8000);
            room2.setAvailable(true);
            roomRepository.save(room2);
        }
    }
}