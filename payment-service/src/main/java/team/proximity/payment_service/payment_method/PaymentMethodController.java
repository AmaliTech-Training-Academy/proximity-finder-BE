package team.proximity.payment_service.payment_method;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.proximity.payment_service.common.ApiSuccessResponse;

@RestController
@RequestMapping("/api/v1/payment-method")
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    public PaymentMethodController(PaymentMethodServiceImpl paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }

    @PostMapping
    ResponseEntity<ApiSuccessResponse> createPaymentMethod(@RequestBody PaymentMethodRequest request) {
        paymentMethodService.createOnePaymentMethod(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiSuccessResponse("Payment Method added successfully", true));
    }
}
