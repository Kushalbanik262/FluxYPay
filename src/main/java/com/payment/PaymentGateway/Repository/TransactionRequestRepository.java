package com.payment.PaymentGateway.Repository;

import com.payment.PaymentGateway.Model.Tables.TransactionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRequestRepository extends JpaRepository<TransactionRequest,String> {
}
