package au.com.belong.phone.management.controller;

import au.com.belong.phone.management.dto.PhoneActivationRequest;
import au.com.belong.phone.management.dto.PhoneNumbersResponse;
import au.com.belong.phone.management.model.PhoneNumberDetails;
import au.com.belong.phone.management.service.PhoneNumbersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/phone-api/phone-numbers")
@Tag(name = "Phone Numbers Management", description = "Managing Phone Numbers With an API")
public class PhoneNumbersController {

    private final PhoneNumbersService phoneNumbersService;

    public PhoneNumbersController(PhoneNumbersService phoneNumbersService) {
        this.phoneNumbersService = phoneNumbersService;
    }

    @Operation(
            summary = "Get all phone numbers",
            description = "Retrieves a list of all phone numbers in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Phone numbers retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<PhoneNumberDetails>> getAllPhoneNumbers() {
        return ResponseEntity.ok(phoneNumbersService.getAllPhoneNumbers());
    }

    @Operation(
            summary = "Get a customer's phone number(s)",
            description = "Retrieves a list of all phone numbers for a specific customer"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Phone numbers for customer retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @GetMapping("/customer/{customerNameOrEmailId}")
    public ResponseEntity<List<PhoneNumbersResponse>> getPhoneNumbersForCustomer(
            @PathVariable String customerNameOrEmailId) {
        return ResponseEntity.ok(phoneNumbersService.getPhoneNumbersForCustomer(customerNameOrEmailId));
    }

    @Operation(
            summary = "Activate a phone number for a Customer",
            description = "Activates a specific phone number to a customer if it exists and is not already active"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New Phone number activated successfully"),
            @ApiResponse(responseCode = "201", description = "Existing number activated successfully"),
            @ApiResponse(responseCode = "400", description = "Phone number is already active"),
            @ApiResponse(responseCode = "404", description = "Phone number not found")
    })
    @PostMapping("/activate")
    public ResponseEntity<Void> activatePhoneNumber(
            @Valid
            @RequestBody
            PhoneActivationRequest phoneActivationRequest) {
        phoneNumbersService.activatePhoneNumberForCustomer(phoneActivationRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}