package team.proximity.provider_profile_service.bank;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Banks", description = "Operations related to banks")
@RestController
@RequestMapping("/api/v1/provider-service/banks")
public class BankController {

    private final BankServiceImpl bankService;

    public BankController(BankServiceImpl bankService) {
        this.bankService = bankService;
    }

    @Operation(summary = "Get a list of all banks")
    @GetMapping
    public ResponseEntity<List<BankResponse>> getAllBanks() {
        List<BankResponse> banks = bankService.getAllBanks();
        return ResponseEntity.ok(banks);
    }
}
