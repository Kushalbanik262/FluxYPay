package com.payment.PaymentGateway.Services.Implementations;

import com.payment.PaymentGateway.Exceptions.IdempotencyException;
import com.payment.PaymentGateway.Exceptions.MaliciousTransactionException;
import com.payment.PaymentGateway.Exceptions.TokenInvalidException;
import com.payment.PaymentGateway.Exceptions.TransactionException;
import com.payment.PaymentGateway.Model.Payment.IdempotencyToken;
import com.payment.PaymentGateway.Model.Payment.PaymentVerificationResult;
import com.payment.PaymentGateway.Model.Payment.TRANSACTION_STATUS;
import com.payment.PaymentGateway.Model.Tables.TransactionRequest;
import com.payment.PaymentGateway.Repository.IdempotencyRepository;
import com.payment.PaymentGateway.Repository.TransactionRepository;
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
    private TransactionRepository transactionRepository;
    @Autowired
    private IdempotencyRepository idempotencyRepository;

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
        return null;
    }

    /**
     * Builds Payment verification Result
     * @return
     */
    private PaymentVerificationResult paymentVerificationResultBuilder(){
        return null;
    }

    /**
     *
     * @param transactionId The Current Transaction Id
     * @return Validity of the Transaction
     */
    private boolean checkExistingRequestCollision(String transactionId){
            try {
                TransactionRequest currentTransactionRequest = transactionRepository.getTransactionRequestById(transactionId);
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
     * @param idempotencyToken Token to validate
     */
    private void validateIdempotency(IdempotencyToken idempotencyToken){
        IdempotencyToken token = idempotencyRepository.getIdempotencyTokenById(idempotencyToken.getId());
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
            throw new MaliciousTransactionException("Malicious Idempotency Token Detected During Transaction");
        }
    }

    private void checkValidity(IdempotencyToken idempotencyToken){
        if(idempotencyToken.isMarkedAsInvalid()){
            throw new TokenInvalidException("Token is invalid");
        }
    }

    private void checkTimeConstrain(IdempotencyToken token){
        if(token.getExpiredAt().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Token Expired");
        }
        // TODO: Check Consistency by some DB method
        if(token.getLastProcessingTime().isAfter(LocalDateTime.now())){
            throw new RuntimeException("Consistency Issue");
        }
    }
}
