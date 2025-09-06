package com.payment.PaymentGateway.Repository;

import com.payment.PaymentGateway.Model.Tables.PaymentRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRequestRepository extends JpaRepository<PaymentRequest,String> {
}
