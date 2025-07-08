package com.example.estoque.services.OrderServices;

import com.example.estoque.entities.OrderEntities.Order;
import com.example.estoque.entities.OrderEntities.OrderStatus;
import com.example.estoque.entities.expenseEntities.Expense;
import com.example.estoque.entities.expenseEntities.ExpenseStatus;
import com.example.estoque.repositories.OrderRepository;
import com.example.estoque.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderNotificationService {

    private final OrderRepository orderRepository;

    private final EmailService emailService;

    //Email notification for orders that are about to become overdue
    @Scheduled(cron = "0 0 */6 * * *")
    public void notifyUpcomingOverdueOrders() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Order> orders = orderRepository.findByOrdstsAndOrdpaydueAndIsDeletedFalse(OrderStatus.PENDING, tomorrow);

        orders.forEach(order -> {
            String userEmail = order.getCreatedBy();
            String subject = "Upcoming Overdue Order";
            String body = String.format(
                    "Hi!\n\n" +
                            "This is a reminder that your order \" (due date: %s) is about to become OVERDUE.\n" +
                            "To avoid any penalties or issues with fulfillment, please ensure this order is paid by tomorrow.\n\n" +
                            "Thank you for using ManageIt!",
                    order.getOrdpaydue()
            );

            emailService.sendSimpleMessage(userEmail, subject, body);

        });
    }

    //Email notification for overdue orders
    @Scheduled(cron = "0 * * * * *")
    public void notifyOverdueOrders() {
        List<Order> overdueOrders = orderRepository.findByOrdstsAndIsDeletedFalse(OrderStatus.OVERDUE);

        overdueOrders.forEach(order -> {
            String userEmail = order.getCreatedBy();
            String subject = "Upcoming Overdue Order";
            String body = String.format(
                    "Hi!\n\n" +
                            "This is a reminder that your order \" (due date: %s) is OVERDUE.\n" +
                            "Please pay this order as soon as possible to avoid further penalties or disruptions.\n\n" +
                            "Thank you for using ManageIt!",
                    order.getOrdpaydue()
            );

            emailService.sendSimpleMessage(userEmail, subject, body);

        });
    }
}
