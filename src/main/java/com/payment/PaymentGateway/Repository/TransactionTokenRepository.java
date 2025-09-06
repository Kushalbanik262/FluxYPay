package com.payment.PaymentGateway.Repository;

import com.payment.PaymentGateway.Model.Tables.TransactionToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionTokenRepository extends JpaRepository<TransactionToken,String> {
}
