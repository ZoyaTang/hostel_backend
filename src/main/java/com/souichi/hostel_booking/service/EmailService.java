package com.souichi.hostel_booking.service;

import com.souichi.hostel_booking.model.Booking;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendBookingConfirmation(Booking booking) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(booking.getGuestEmail());
        message.setSubject("Booking Confirmation - Souichi Hostel");

        message.setText(
                "Booking confirmed!\n\n" +
                "Room ID: " + booking.getRoomId() + "\n" +
                "Check-in: " + booking.getCheckIn() + "\n" +
                "Check-out: " + booking.getCheckOut() + "\n" +
                "Total: ¥" + booking.getTotalPrice() + "\n\n" +
                "Please complete payment to confirm your stay.\n\n" +
                "Thank you!"
        );

        mailSender.send(message);
    }
}
