package com.example.bankingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProjectedInvestmentDto {
    private int year;
    private String startingAmount;
    private String interest;
    private String endingBalance;

    public ProjectedInvestmentDto(int year, double startingAmount, double interest, double endingBalance) {
        this.year = year;
        this.startingAmount = String.format("%.2f", startingAmount);
        this.interest = String.format("%.2f", interest);
        this.endingBalance = String.format("%.2f", endingBalance);
    }
}
