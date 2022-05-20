package ru.aliex;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Считывание содержимого JSON файла.
 * При вызове этой программы следует указать имя файла,
 * содержимое которого требуется считать, например
 * необходимо ввести в командной строке:
 * java TicketsApp tickets.json
 */
public class TicketsApp {
    public static void main( String[] args ) throws IOException {

        if(args.length != 1) {
            System.out.println("< Использование: java TicketsApp имя_файла.json >");
            return;
        }

        long result = 0;
        ObjectMapper mapper = new ObjectMapper();

        TicketsList tickets = mapper.readValue(new File(args[0]), TicketsList.class);
        List<Ticket> ticketList = tickets.getTickets();

        for(Ticket ticket: ticketList) {
            result += getDiffTime(ticket.getDeparture_time(), ticket.getArrival_time());
        }

        LocalTime middleTime = LocalTime.ofSecondOfDay(result / ticketList.size());
        LocalTime percentTime = LocalTime.ofSecondOfDay((long) ((result / ticketList.size()) * 0.9));

        System.out.println("Среднее время полета между городами Владивосток" +
                " и Тель-Авив = " + middleTime);
        System.out.println("90% времени полета между городами" +
                " Владивосток и Тель-Авив = " + percentTime);
    }

    private static long getDiffTime(String departureTime, String arrivalTime) {

        LocalTime departure = getParseTime(departureTime);
        LocalTime arrival = getParseTime(arrivalTime);

        return Duration.between(departure, arrival).getSeconds();
    }

    private static LocalTime getParseTime(String time) {

        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("H:mm");

        try {
            return LocalTime.parse(time);
        } catch (DateTimeParseException ex) {
            return LocalTime.parse(time, pattern);
        }
    }
}
