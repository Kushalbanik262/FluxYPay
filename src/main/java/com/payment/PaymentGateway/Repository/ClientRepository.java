package com.payment.PaymentGateway.Repository;

import com.payment.PaymentGateway.Model.Tables.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client,String> {
}
