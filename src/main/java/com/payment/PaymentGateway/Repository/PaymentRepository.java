package com.payment.PaymentGateway.Repository;

import com.payment.PaymentGateway.Model.Tables.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,String> {
}
