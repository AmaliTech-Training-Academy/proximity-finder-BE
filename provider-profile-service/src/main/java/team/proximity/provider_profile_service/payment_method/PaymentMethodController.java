package team.proximity.provider_profile_service.payment_method;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.proximity.provider_profile_service.common.ApiSuccessResponse;

import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/api/v1/provider-service/payment-method")
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    public PaymentMethodController(PaymentMethodServiceImpl paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }


    @GetMapping
    public List<PaymentMethodResponse> getPaymentMethodsForAuthenticatedUser() {
        return paymentMethodService.getPaymentMethodsForAuthenticatedUser();
    }


    @GetMapping("/providers/mobile-money-providers")
    public List<MobileMoneyServiceProvider> getMobileMoneyProviders() {
        return Arrays.asList(MobileMoneyServiceProvider.values());
    }


    @PostMapping
    ResponseEntity<ApiSuccessResponse> createPaymentMethod(@Valid @RequestBody PaymentMethodRequest request) {
        paymentMethodService.createNewPaymentMethod(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiSuccessResponse("Payment Method added successfully", true));
    }



    @PostMapping("/new-payment-method")
    ResponseEntity<ApiSuccessResponse> createAnotherPaymentMethod(@Valid @RequestBody PaymentMethodRequest request) {
        paymentMethodService.createAnotherPaymentMethod(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiSuccessResponse("Payment Method added successfully", true));
    }


    @PatchMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse> updatePaymentMethod(@Valid @PathVariable Long id, @RequestBody PaymentMethodRequest request) {
        paymentMethodService.updatePaymentMethod(request, id);
        return new ResponseEntity<>(new ApiSuccessResponse("Payment Method updated successfully", true), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse> deletePaymentMethodById(@PathVariable Long id) {
        paymentMethodService.deletePaymentMethodById(id);
        return new ResponseEntity<>(new ApiSuccessResponse("Payment Method deleted successfully", true), HttpStatus.OK);
    }


}
