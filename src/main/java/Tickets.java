import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

public class Tickets {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            TicketWrapper wrapper = mapper.readValue(new File("tickets.json"), TicketWrapper.class);
            TicketClass[] tickets = wrapper.getTickets();

            List filteresFlights = filter_flights(tickets);

            List flight_duration = calculate_duration(filteresFlights);
            flight_duration.forEach(ticket -> {
                try {
                    System.out.println(mapper.writeValueAsString(ticket));
                } catch (JsonProcessingException e) {
                    System.out.println(ticket.toString());
                }
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<TicketClass> calculate_duration(List<TicketClass> tickets) {
        return tickets.stream()
                .map(ticket -> {
                    if (ticket != null) {
                        long duration = calculate_flight_duration(
                                ticket.getDeparture_time(),
                                ticket.getArrival_time()
                        );
                        ticket.setDuration(duration);
                    }

                    return ticket;
                }).collect(Collectors.toList());
    }


    private static long calculate_flight_duration(String departureTime, String arrivalTime) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");

            LocalTime depTime = LocalTime.parse(departureTime, formatter);
            LocalTime arrTime = LocalTime.parse(arrivalTime, formatter);

            long durationMinutes = ChronoUnit.MINUTES.between(depTime, arrTime);

            // проверка на разницу в сутках
            if (durationMinutes < 0) {
                durationMinutes += 24 * 60; // + сутки
            }

            return durationMinutes;
        } catch (Exception e) {
            return -1;
        }
    }

    private static List filter_flights(TicketClass[] tickets) {
        //Владивосток и Тель-Авив
        List<TicketClass> flight = stream(tickets)
                .filter(ticket -> ticket != null &&
                        (("VVO".equals(ticket.getOrigin()) && "TLV".equals(ticket.getDestination())) ||
                                ("TLV".equals(ticket.getOrigin()) && "VVO".equals(ticket.getDestination()))))
                .toList();

        return flight;
    }
}

