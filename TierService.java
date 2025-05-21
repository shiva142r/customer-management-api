
package com.example.customermanagement.service;

import com.example.customermanagement.model.Customer;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class TierService {

    public String calculateTier(Customer customer) {
        if (customer.getAnnualSpend() == null) return "Silver";

        BigDecimal spend = customer.getAnnualSpend();
        LocalDateTime purchaseDate = customer.getLastPurchaseDate();

        if (spend.compareTo(new BigDecimal("10000")) >= 0 && purchaseDate != null &&
            purchaseDate.isAfter(LocalDateTime.now().minus(6, ChronoUnit.MONTHS))) {
            return "Platinum";
        } else if (spend.compareTo(new BigDecimal("1000")) >= 0 && 
                   purchaseDate != null && 
                   purchaseDate.isAfter(LocalDateTime.now().minus(12, ChronoUnit.MONTHS))) {
            return "Gold";
        }
        return "Silver";
    }
}
