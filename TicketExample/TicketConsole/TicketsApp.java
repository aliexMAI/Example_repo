import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.*;

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
            System.out.println("\n>> Using: java -cp jackson-databind-2.13.1.jar;" +
			"jackson-core-2.13.3.jar;jackson-annotations-2.13.3.jar TicketsApp.java name_file.json ");
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

        System.out.println("\n>> Average flight time between Vladivostok and Tel Aviv = " + middleTime);
        System.out.println("\n>> 90% of the flight time between Vladivostok and Tel Aviv = " + percentTime);
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

/**
 * Класс реализующий объект ticket
 */
class Ticket {

    private String origin;
    private String origin_name;
    private String destination;
    private String destination_name;
    private String departure_date;
    private String departure_time;
    private String arrival_date;
    private String arrival_time;
    private String carrier;
    private int stops;
    private int price;

    public Ticket() {}

    public String getOrigin() {
        return origin;
    }

    public String getOrigin_name() {
        return origin_name;
    }

    public String getDestination() {
        return destination;
    }

    public String getDestination_name() {
        return destination_name;
    }

    public String getDeparture_date() {
        return departure_date;
    }

    public String getDeparture_time() {
        return departure_time;
    }

    public String getArrival_date() {
        return arrival_date;
    }

    public String getArrival_time() {
        return arrival_time;
    }

    public String getCarrier() {
        return carrier;
    }

    public int getStops() {
        return stops;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "origin='" + origin + '\'' +
                ", origin_name='" + origin_name + '\'' +
                ", destination='" + destination + '\'' +
                ", destination_name='" + destination_name + '\'' +
                ", departure_date='" + departure_date + '\'' +
                ", departure_time='" + departure_time + '\'' +
                ", arrival_date='" + arrival_date + '\'' +
                ", arrival_time='" + arrival_time + '\'' +
                ", carrier='" + carrier + '\'' +
                ", stops=" + stops +
                ", price=" + price +
                '}';
    }
}


/**
 * Класс реализующий список объектов tickets
 */
class TicketsList {

    private List<Ticket> tickets;

    public TicketsList() {
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    @Override
    public String toString() {
        return "TicketsList{" + tickets + "}";
    }
}

