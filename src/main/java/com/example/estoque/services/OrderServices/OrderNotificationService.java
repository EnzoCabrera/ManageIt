package com.example.estoque.services.OrderServices;

import com.example.estoque.entities.OrderEntities.Order;
import com.example.estoque.entities.OrderEntities.OrderStatus;
import com.example.estoque.entities.stockEntities.Stock;
import com.example.estoque.repositories.OrderRepositories.OrderRepository;
import com.example.estoque.services.AuditLogService;
import com.example.estoque.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderNotificationService {

    private final OrderRepository orderRepository;

    private final EmailService emailService;

    @Autowired
    private AuditLogService auditLogService;

    //Email notification for orders that are about to become overdue
    @Scheduled(cron = "0 0 */6 * * *")
    public void notifyUpcomingOverdueOrders() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Order> orders = orderRepository.findByOrdstsAndOrdpaydueAndIsDeletedFalseFetchCustomer(OrderStatus.PENDING, tomorrow);

        orders.forEach(order -> {
            String customerEmail = order.getCodcus().getCusemail();
            String subject = "Upcoming Overdue Order";
            String body = String.format(
                    "Hi!\n\n" +
                            "This is a reminder that your order \" (due date: %s) is about to become OVERDUE.\n" +
                            "To avoid any penalties or issues with fulfillment, please ensure this order is paid by tomorrow.\n\n" +
                            "Thank you for using ManageIt!",
                    order.getOrdpaydue()
            );

            emailService.sendSimpleMessage(customerEmail, subject, body);

        });
    }

    //Email notification for overdue orders
    @Scheduled(cron = "0 0 */6 * * *")
    public void notifyOverdueOrders() {
        List<Order> overdueOrders = orderRepository.findByOrdstsAndIsDeletedFalseFetchCustomer(OrderStatus.OVERDUE);

        overdueOrders.forEach(order -> {
            String customerEmail = order.getCodcus().getCusemail();
            String subject = "Upcoming Overdue Order";
            String body = String.format(
                    "Hi!\n\n" +
                            "This is a reminder that your order \" (due date: %s) is OVERDUE.\n" +
                            "Please pay this order as soon as possible to avoid further penalties or disruptions.\n\n" +
                            "Thank you for using ManageIt!",
                    order.getOrdpaydue()
            );

            emailService.sendSimpleMessage(customerEmail, subject, body);

        });
    }

    //Email notification for stock that are below minimum quantity
    public void notifyLowStock(Stock stock) {
        String email = stock.getCreatedBy();

        if (email == null || !email.contains("@")) return;

        String msg = String.format("Product %s are below minimum quantity! Current: %d | Minimum: %d",
                stock.getProductName(), stock.getQuantity(), stock.getMinimumQtd());

        emailService.sendSimpleMessage(
                email,
                "Low Stock Alert",
                msg);
        auditLogService.log(
                "Stock",
                stock.getCodProd(),
                "ALERT",
                "quantity below minimum",
                null,
                msg,
                stock.getCreatedBy()
        );
    }
}
