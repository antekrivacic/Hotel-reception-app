package com.reception.projektkrivacic.model.classes;

import java.math.BigDecimal;

public class HotelFinance {
    private Long hotelFinanceId;
    private BigDecimal totalIncomeYearly;
    private BigDecimal totalExpenseYearly;
    private BigDecimal totalProfitYearly;

    public HotelFinance(Long id, BigDecimal totalIncomeYearly, BigDecimal totalExpenseYearly) {
        this.hotelFinanceId = id;
        this.totalIncomeYearly = totalIncomeYearly;
        this.totalExpenseYearly = totalExpenseYearly;
        this.totalProfitYearly = totalIncomeYearly.subtract(totalExpenseYearly);
    }

    public void addIncome(BigDecimal income) {
        this.totalIncomeYearly = this.totalIncomeYearly.add(income);
    }
    public void addExpense(BigDecimal expense) {
        this.totalExpenseYearly = this.totalExpenseYearly.add(expense);
    }

    public Long getHotelFinanceId() {
        return hotelFinanceId;
    }
    public BigDecimal getTotalIncomeYearly() {
        return totalIncomeYearly;
    }
    public BigDecimal getTotalExpenseYearly() {
        return totalExpenseYearly;
    }
    public BigDecimal getTotalProfitYearly() {
        return totalProfitYearly;
    }

}
