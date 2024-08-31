package com.example.ElearningTLU.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity

public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String invoiceId;

    private String semesterGroupId;

    private double totalCost;

    private LocalDate timePayment;

    private PaymentStatus paymentStatus;

    @ManyToOne
    @JoinColumn(name = "personId")
    private Student student;

    @OneToMany(mappedBy = "invoice")
    List<InvoiceDetail> invoiceDetailList = new ArrayList<>();
}
