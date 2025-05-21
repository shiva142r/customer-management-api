
package com.example.customermanagement;

import com.example.customermanagement.model.Customer;
import com.example.customermanagement.service.TierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TierServiceTest {

    private TierService tierService;

    @BeforeEach
    public void setUp() {
        tierService = new TierService();
    }

    @Test
    public void testSilverTier() {
        Customer c = new Customer();
        c.setAnnualSpend(new BigDecimal("500"));
        c.setLastPurchaseDate(LocalDateTime.now().minusMonths(20));
        assertEquals("Silver", tierService.calculateTier(c));
    }

    @Test
    public void testGoldTier() {
        Customer c = new Customer();
        c.setAnnualSpend(new BigDecimal("1500"));
        c.setLastPurchaseDate(LocalDateTime.now().minusMonths(6));
        assertEquals("Gold", tierService.calculateTier(c));
    }

    @Test
    public void testPlatinumTier() {
        Customer c = new Customer();
        c.setAnnualSpend(new BigDecimal("15000"));
        c.setLastPurchaseDate(LocalDateTime.now().minusMonths(3));
        assertEquals("Platinum", tierService.calculateTier(c));
    }

    @Test
    public void testDefaultSilverWhenNoSpend() {
        Customer c = new Customer();
        assertEquals("Silver", tierService.calculateTier(c));
    }
}
