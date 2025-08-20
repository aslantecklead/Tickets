import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class Tickets {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            TicketWrapper wrapper = mapper.readValue(new File("tickets.json"), TicketWrapper.class);
            TicketClass[] tickets = wrapper.getTickets();

            List<TicketClass> filteredFlights = filter_flights(tickets);
            List<TicketClass> flight_duration = calculate_duration(filteredFlights);

            // фильтрация рейсов по длительности полёта
            List<TicketClass> validFlights = flight_duration.stream()
                    .filter(ticket -> ticket != null && ticket.getDuration() >= 0)
                    .collect(Collectors.toList());

            Map<String, Optional<Long>> minDurationByCarrier = validFlights.stream()
                    .collect(Collectors.groupingBy(
                            TicketClass::getCarrier,
                            Collectors.mapping(
                                    TicketClass::getDuration,
                                    Collectors.minBy(Comparator.naturalOrder())
                            )
                    ));
            System.out.println("Минимальное время полета для каждого авиаперевозчика:");
            minDurationByCarrier.forEach((carrier, minDuration) -> {
                if (minDuration.isPresent()) {
                    long minutes = minDuration.get();
                    long hours = minutes / 60;
                    long remainingMinutes = minutes % 60;
                    System.out.printf("Перевозчик %s: %d часов %d минут (%d минут)%n",
                            carrier, hours, remainingMinutes, minutes);
                }
            });

            List<Integer> prices = validFlights.stream()
                    .map(TicketClass::getPrice)
                    .filter(price -> price != null)
                    .sorted()
                    .collect(Collectors.toList());

            if (prices.isEmpty()) {
                System.out.println("Нет данных о ценах для расчета");
                return;
            }

            // средняя цена с проверкой
            OptionalDouble averageOpt = prices.stream()
                    .mapToInt(Integer::intValue)
                    .average();

            if (!averageOpt.isPresent()) {
                System.out.println("Не удалось рассчитать среднюю цену");
                return;
            }

            double averagePrice = averageOpt.getAsDouble();

            // медианная цена
            double medianPrice = calculateMedian(prices);
            // разница между средней ценой и медианой
            double difference = averagePrice - medianPrice;

            System.out.printf("Средняя цена: %.2f рублей%n", averagePrice);
            System.out.printf("Медианная цена: %.2f рублей%n", medianPrice);
            System.out.printf("Разница между средней и медианной ценой: %.2f рублей%n", difference);

        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ошибка выполнения: " + e.getMessage());
        }
    }

    // отсортирвоанный по возрастанию список цен
    private static double calculateMedian(List<Integer> prices) {
        int size = prices.size();
        if (size == 0) return 0.0;

        if (size % 2 == 0) {
            // четное количество элементов - среднее двух центральных
            int mid1 = prices.get(size / 2 - 1);
            int mid2 = prices.get(size / 2);
            return (mid1 + mid2) / 2.0;
        } else {
            // нечетное количество элементов - центральный элемент
            return prices.get(size / 2);
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
            System.err.println("Ошибка парсинга времени: " + e.getMessage());
            return -1;
        }
    }

    private static List<TicketClass> filter_flights(TicketClass[] tickets) {
        //Владивосток и Тель-Авив
        List<TicketClass> flight = stream(tickets)
                .filter(ticket -> ticket != null &&
                        (("VVO".equals(ticket.getOrigin()) && "TLV".equals(ticket.getDestination())) ||
                                ("TLV".equals(ticket.getOrigin()) && "VVO".equals(ticket.getDestination()))))
                .collect(Collectors.toList());

        return flight;
    }
}