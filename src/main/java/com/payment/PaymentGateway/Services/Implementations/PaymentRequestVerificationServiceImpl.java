package com.payment.PaymentGateway.Services.Implementations;

import com.payment.PaymentGateway.Exceptions.IdempotencyException;
import com.payment.PaymentGateway.Exceptions.MaliciousTransactionException;
import com.payment.PaymentGateway.Exceptions.TokenInvalidException;
import com.payment.PaymentGateway.Exceptions.TransactionException;
import com.payment.PaymentGateway.Model.Tables.IdempotencyToken;
import com.payment.PaymentGateway.Model.Payment.PaymentVerificationResult;
import com.payment.PaymentGateway.Model.Payment.TRANSACTION_STATUS;
import com.payment.PaymentGateway.Model.Tables.PaymentNetwork;
import com.payment.PaymentGateway.Model.Tables.TransactionRequest;
import com.payment.PaymentGateway.Repository.IdempotencyTokenRepository;
import com.payment.PaymentGateway.Repository.TransactionRequestRepository;
import com.payment.PaymentGateway.Services.ClientVerificationService;
import com.payment.PaymentGateway.Services.PaymentNetworkValidator;
import com.payment.PaymentGateway.Services.PaymentRequestVerificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * verifies for any duplicate payment request or the current request in serving
 */
@Service
public class PaymentRequestVerificationServiceImpl implements PaymentRequestVerificationService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentRequestVerificationServiceImpl.class);

    @Autowired
    private TransactionRequestRepository transactionRequestRepository;
    @Autowired
    private IdempotencyTokenRepository idempotencyTokenRepository;

    @Autowired
    private ClientVerificationService clientVerificationService;

    @Autowired
    private PaymentNetworkValidator paymentNetworkValidator;

    /**
     * verifies any payment request
     * @param transactionRequest request for the verification
     */
    @Override
    public PaymentVerificationResult verifyPayment(TransactionRequest transactionRequest, IdempotencyToken idempotencyToken) {
        /*
         * 1. Check the state of the transaction
         * 2. If Present and Pending Return Already in progress state
         * 3. Else Throw Duplicate Payment Exception
         * ------------------------------------------------------------
         * 1. Check Idempotency Logic
         * --------------------------------------------------------------
         * 4. Check with Bank network if the account contains the money
         * 5. If bank is unavailable or not sufficient money throw exception
         * 6. If user not valid or something  or blacklisted from gateway
         */
        if(!checkExistingRequestCollision(transactionRequest.getId())){
            throw new TransactionException("Transaction failed");
        }
        validateIdempotency(idempotencyToken);
        clientVerificationService.verifyClient(idempotencyToken.getClient());
        verifyPaymentNetwork(transactionRequest.getTransaction().getPayment().getPaymentDetails().getPaymentNetwork());
        return null;
    }

    /**
     * Builds Payment verification Result
     * @return
     */
    private PaymentVerificationResult paymentVerificationResultBuilder(){
        return null;
    }

    private void verifyPaymentNetwork(PaymentNetwork paymentNetwork){
        paymentNetworkValidator.validatePaymentNetwork(paymentNetwork);
    }

    /**
     *
     * @param transactionId The Current Transaction Id
     * @return Validity of the Transaction
     */
    private boolean checkExistingRequestCollision(String transactionId){
            try {
                TransactionRequest currentTransactionRequest = transactionRequestRepository.getTransactionRequestById(transactionId);
                if(currentTransactionRequest == null){return true;}
                // If the current Transaction is failed
                if(currentTransactionRequest.getTransactionStatus() != TRANSACTION_STATUS.ERROR){
                    logger.error("Similar ID with transaction is already available");
                    throw new IdempotencyException("Transaction with Id:"+currentTransactionRequest.getId()+" Already Exists");
                }
            }
            catch (Exception ex){
                if(! (ex instanceof IdempotencyException)){throw new RuntimeException(ex.getMessage());}

            }
        return false;
    }

    /**
     * Idempotency Validator
     *
     * @param idempotencyToken TransactionToken to validate
     */
    private void validateIdempotency(IdempotencyToken idempotencyToken){
        IdempotencyToken token = idempotencyTokenRepository.getById(idempotencyToken.getId());
        if(token == null){throw new RuntimeException("Idempotency token not found");}
        checkTimeConstrain(idempotencyToken);
        checkValidity(idempotencyToken);
        checkIfMalicious(idempotencyToken);
    }

    /**
     * TODO: Please decline the payment and abort all the operations here as some malicious author tried to do transaction
     * @param idempotencyToken token
     */
    private void checkIfMalicious(IdempotencyToken idempotencyToken){
        if(idempotencyToken.isMarkedAsMalicious()){
            throw new MaliciousTransactionException("Malicious Idempotency TransactionToken Detected During Transaction");
        }
    }

    private void checkValidity(IdempotencyToken idempotencyToken){
        if(idempotencyToken.isMarkedAsInvalid()){
            throw new TokenInvalidException("TransactionToken is invalid");
        }
    }

    private void checkTimeConstrain(IdempotencyToken token){
        if(token.getExpiredAt().isBefore(LocalDateTime.now())){
            throw new RuntimeException("TransactionToken Expired");
        }
    }
}
