package com.example.estoque.services.StockServices;

import com.example.estoque.entities.stockEntities.Stock;
import com.example.estoque.repositories.StockRepository;
import com.example.estoque.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockNotificationService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private EmailService emailService;

    //Email notification for low stock products
    @Scheduled(cron = "0 0 * * * *")
    public void checkStockLevels() {
        List<Stock> lowStocks = stockRepository.findAllBelowMinimum();
        for (Stock stock : lowStocks) {
            String customerEmail = stock.getCreatedBy();
            String subject = "Low Stock Alert for " + stock.getProductName();
            String body = String.format(
                    "Hi!\n\n" +
                            "This is a reminder that the stock for product \"%s\" is below the minimum threshold.\n" +
                            "Current quantity: %d\n" +
                            "Minimum required: %d\n\n" +
                            "Please take action to replenish the stock as soon as possible.\n\n" +
                            "Thank you for using ManageIt!",
                    stock.getProductName(),
                    stock.getQuantity(),
                    stock.getMinimumQtd()
            );

            emailService.sendSimpleMessage(customerEmail, subject, body);
        }
    }
}
