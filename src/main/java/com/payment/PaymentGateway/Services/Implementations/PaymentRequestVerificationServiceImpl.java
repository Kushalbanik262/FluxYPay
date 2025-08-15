package com.payment.PaymentGateway.Services.Implementations;

import com.payment.PaymentGateway.Model.Payment.PaymentVerificationResult;
import com.payment.PaymentGateway.Model.Tables.TransactionRequest;
import com.payment.PaymentGateway.Repository.TransactionRepository;
import com.payment.PaymentGateway.Services.PaymentRequestVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * verifies for any duplicate payment request or the current request in serving
 */
@Service
public class PaymentRequestVerificationServiceImpl implements PaymentRequestVerificationService {

    @Autowired
    private TransactionRepository transactionRepository;


    /**
     * verifies any payment request
     * @param paymentRequest request for the verification
     */
    @Override
    public PaymentVerificationResult verifyPayment(TransactionRequest paymentRequest) {
        /**
         * 1. Check the state of the transaction
         * 2. If Present and Pending Return Already in progress state
         * 3. Else Throw Duplicate Payment Exception
         * --------------------------------------------------------------
         * 4. Check with Bank network if the account contains the money
         * 5. If bank is unavailable or not sufficient money throw exception
         * 6. If user not valid or something  or blacklisted from gateway
         */
        return null;
    }

    /**
     *
     * @param transactionId
     * @return
     */
    private boolean checkExistingRequestCollision(String transactionId){
            try {
                TransactionRequest currentTransactionRequest = transactionRepository.getTransactionRequestById(transactionId);
                //if(currentTransactionRequest.get)
            }
            catch (Exception ex){

            }
        return false;
    }
}
