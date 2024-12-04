package team.proximity.provider_profile_service.payment_method;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.proximity.provider_profile_service.common.ApiSuccessResponse;

import java.util.Arrays;
import java.util.List;

@Tag(name = "Payment Methods", description = "Operations related to payment methods")
@RestController
@RequestMapping("/api/v1/provider-service/payment-method")
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    public PaymentMethodController(PaymentMethodServiceImpl paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }

    @Operation(summary = "Get a list of all payment methods for the authenticated user")
    @SecurityRequirement(name = "BearerAuth")
    @GetMapping
    public List<PaymentMethodResponse> getPaymentMethodsForAuthenticatedUser() {
        return paymentMethodService.getPaymentMethodsForAuthenticatedUser();
    }

    @Operation(summary = "Get a list of all accepted mobile money providers")
    @GetMapping("/providers/mobile-money-providers")
    public List<MobileMoneyServiceProvider> getMobileMoneyProviders() {
        return Arrays.asList(MobileMoneyServiceProvider.values());
    }

    @Operation(
            summary = "Add Payment Method During Registration",
            description = "Create a new payment method based on the payment preference selected",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Payment Method Request",
                    content = @Content(
                            schema = @Schema(implementation = PaymentMethodRequest.class),
                            examples = {
                                    @ExampleObject(name = "Bank Account Example", value = "{\n  \"paymentPreference\": \"Bank Account\",\n  \"bankName\": \"Sample Bank\",\n  \"accountNumber\": \"123456789\",\n  \"accountName\": \"John Doe\",\n  \"accountAlias\": \"JD123\"\n}"),
                                    @ExampleObject(name = "Paypal Example", value = "{\n  \"paymentPreference\": \"Paypal\",\n  \"accountNumber\": \"john.doe@example.com\",\n  \"accountName\": \"John Doe\",\n  \"accountAlias\": \"JD123\"\n}"),
                                    @ExampleObject(name = "Mobile Money Example", value = "{\n  \"paymentPreference\": \"Mobile Money\",\n  \"serviceProvider\": \"MTN\",\n  \"accountName\": \"John Doe\",\n  \"accountAlias\": \"JD123\",\n  \"mobileNumber\": \"0551234567\"\n}")
                            }
                    )
            )
    )
    @SecurityRequirement(name = "BearerAuth")
    @PostMapping
    ResponseEntity<ApiSuccessResponse> createPaymentMethod(@Valid @RequestBody PaymentMethodRequest request) {
        paymentMethodService.createNewPaymentMethod(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiSuccessResponse("Payment Method added successfully", true));
    }


    @Operation(
            summary = "Add a new Payment Method after registration",
            description = "Create a new payment method based on the payment preference selected",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Payment Method Request",
                    content = @Content(
                            schema = @Schema(implementation = PaymentMethodRequest.class),
                            examples = {
                                    @ExampleObject(name = "Bank Account Example", value = "{\n  \"paymentPreference\": \"Bank Account\",\n  \"bankName\": \"Sample Bank\",\n  \"accountNumber\": \"123456789\",\n  \"accountName\": \"John Doe\",\n  \"accountAlias\": \"JD123\"\n}"),
                                    @ExampleObject(name = "Paypal Example", value = "{\n  \"paymentPreference\": \"Paypal\",\n  \"accountNumber\": \"john.doe@example.com\",\n  \"accountName\": \"John Doe\",\n  \"accountAlias\": \"JD123\"\n}"),
                                    @ExampleObject(name = "Mobile Money Example", value = "{\n  \"paymentPreference\": \"Mobile Money\",\n  \"serviceProvider\": \"MTN\",\n  \"accountName\": \"John Doe\",\n  \"accountAlias\": \"JD123\",\n  \"mobileNumber\": \"0551234567\"\n}")
                            }
                    )
            )
    )
    @SecurityRequirement(name = "BearerAuth")
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
