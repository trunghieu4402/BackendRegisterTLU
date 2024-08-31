package com.example.ElearningTLU.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class InvoiceDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String courseId;
    private int  credits;
     private float baseCost;
    private double coefficient;
     private  double totalCost;

     @ManyToOne
    @JoinColumn(name = "invoiceId")
    private Invoice invoice;

}
