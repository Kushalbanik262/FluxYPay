package com.payment.PaymentGateway.Repository;

import com.payment.PaymentGateway.Model.Tables.TransactionRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionRequest,String> {
    public TransactionRequest getTransactionRequestById(String id);
}
