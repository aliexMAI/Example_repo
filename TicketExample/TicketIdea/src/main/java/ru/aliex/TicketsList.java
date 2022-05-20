package ru.aliex;

import java.util.List;

/**
 * Класс реализующий список объектов tickets
 */
public class TicketsList {

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
