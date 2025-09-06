package com.payment.PaymentGateway.Repository;

import com.payment.PaymentGateway.Model.Tables.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
}
