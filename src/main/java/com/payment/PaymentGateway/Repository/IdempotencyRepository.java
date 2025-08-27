package com.payment.PaymentGateway.Repository;

import com.payment.PaymentGateway.Model.Payment.IdempotencyToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 */
@Repository
public interface IdempotencyRepository extends JpaRepository<IdempotencyToken,String> {

    public IdempotencyToken getIdempotencyTokenById(String id);
}
