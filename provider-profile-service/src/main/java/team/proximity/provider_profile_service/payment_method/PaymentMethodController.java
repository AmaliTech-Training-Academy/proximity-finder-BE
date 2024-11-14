package team.proximity.provider_profile_service.payment_method;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.proximity.provider_profile_service.common.ApiSuccessResponse;

import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/api/v1/payment-method")
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    public PaymentMethodController(PaymentMethodServiceImpl paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }

    @PostMapping
    ResponseEntity<ApiSuccessResponse> createPaymentMethod(@RequestBody PaymentMethodRequest request) {
        paymentMethodService.createNewPaymentMethod(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiSuccessResponse("Payment Method added successfully", true));
    }

    @PostMapping("/new-payment-method")
    ResponseEntity<ApiSuccessResponse> createAnotherPaymentMethod(@RequestBody PaymentMethodRequest request) {
        paymentMethodService.createAnotherPaymentMethod(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiSuccessResponse("Payment Method added successfully", true));
    }


    @GetMapping("/payment/mobile-money-providers")
    public List<MobileMoneyServiceProvider> getMobileMoneyProviders() {
        return Arrays.asList(MobileMoneyServiceProvider.values());
    }

}
