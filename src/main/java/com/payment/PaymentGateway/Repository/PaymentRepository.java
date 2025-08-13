package com.payment.PaymentGateway.Repository;

import com.payment.PaymentGateway.Model.Tables.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,String> {
}
