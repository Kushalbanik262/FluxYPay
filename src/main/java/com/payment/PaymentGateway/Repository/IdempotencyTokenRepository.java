package com.payment.PaymentGateway.Repository;

import com.payment.PaymentGateway.Model.Tables.IdempotencyToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdempotencyTokenRepository extends JpaRepository<IdempotencyToken,String> {
}
