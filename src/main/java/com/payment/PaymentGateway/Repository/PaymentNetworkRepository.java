package com.payment.PaymentGateway.Repository;

import com.payment.PaymentGateway.Model.Tables.PaymentNetwork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentNetworkRepository extends JpaRepository<PaymentNetwork,String> {
}
