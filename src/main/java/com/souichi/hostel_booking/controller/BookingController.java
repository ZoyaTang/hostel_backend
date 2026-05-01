package com.souichi.hostel_booking.controller;

import com.souichi.hostel_booking.model.Booking;
import com.souichi.hostel_booking.model.Room;
import com.souichi.hostel_booking.repository.BookingRepository;
import com.souichi.hostel_booking.repository.RoomRepository;
import com.souichi.hostel_booking.service.EmailService; // ✅ NEW
import org.springframework.web.bind.annotation.*;

import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final EmailService emailService; // ✅ NEW

    public BookingController(BookingRepository bookingRepository,
                             RoomRepository roomRepository,
                             EmailService emailService) { // ✅ NEW
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.emailService = emailService; // ✅ NEW
    }

    // GET all bookings
    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // POST create booking
    @PostMapping
    public Map<String, Object> createBooking(@RequestBody Booking booking) {

        Map<String, Object> response = new HashMap<>();

        // 🔴 NEW: check email
        if (booking.getGuestEmail() == null || booking.getGuestEmail().isBlank()) {
            response.put("success", false);
            response.put("message", "Please enter your email.");
            return response;
        }

        // 1️⃣ Validate dates
        if (booking.getCheckOut().isBefore(booking.getCheckIn()) ||
            booking.getCheckOut().isEqual(booking.getCheckIn())) {

            response.put("success", false);
            response.put("message", "Invalid dates!");
            return response;
        }

        // 2️⃣ Check if room exists
        Room room = roomRepository.findById(booking.getRoomId()).orElse(null);

        if (room == null) {
            response.put("success", false);
            response.put("message", "Room not found!");
            return response;
        }

        // 3️⃣ Check availability (no overlap)
        List<Booking> conflicts = bookingRepository
                .findByRoomIdAndCheckOutAfterAndCheckInBefore(
                        booking.getRoomId(),
                        booking.getCheckIn(),
                        booking.getCheckOut()
                );

        if (!conflicts.isEmpty()) {
            response.put("success", false);
            response.put("message", "Room is already booked for these dates!");
            return response;
        }

        // 4️⃣ Calculate nights
        long nights = ChronoUnit.DAYS.between(
                booking.getCheckIn(),
                booking.getCheckOut()
        );

        // 5️⃣ Calculate total price
        double total = nights * room.getPricePerNight();

        booking.setTotalPrice(total);
        booking.setStatus("CONFIRMED");

        // 6️⃣ Save booking
        Booking savedBooking = bookingRepository.save(booking);

        // ✅ 7️⃣ SEND EMAIL (AFTER SAVE)
        try {
            emailService.sendBookingConfirmation(savedBooking);
        } catch (Exception e) {
            System.out.println("Email failed: " + e.getMessage());
        }

        // 8️⃣ Return response
        response.put("success", true);
        response.put("message", "Booking successful! Check your email for confirmation.");
        response.put("booking", savedBooking);

        return response;
    }
}
