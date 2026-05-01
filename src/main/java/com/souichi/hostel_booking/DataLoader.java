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
        if (roomRepository.count() < 3) {
            roomRepository.deleteAll();

            Room room1 = new Room();
            room1.setPricePerNight(5000);
            roomRepository.save(room1);

            Room room2 = new Room();
            room2.setPricePerNight(8000);
            roomRepository.save(room2);

            Room room3 = new Room();
            room3.setPricePerNight(7000);
            roomRepository.save(room3);
        }
    }
}
