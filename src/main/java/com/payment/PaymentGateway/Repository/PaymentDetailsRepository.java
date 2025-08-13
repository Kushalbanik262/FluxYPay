package com.payment.PaymentGateway.Repository;

import com.payment.PaymentGateway.Model.Tables.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails,String> {

}
