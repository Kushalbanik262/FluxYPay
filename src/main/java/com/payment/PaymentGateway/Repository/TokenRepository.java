package com.payment.PaymentGateway.Repository;

import com.payment.PaymentGateway.Model.Auth.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token,String> {
}
