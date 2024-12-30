package model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Loan {
    private int loanId;
    private int bookId;
    private int userId;
    private LocalDate loanDate;
    private LocalDate returnDate;
}
