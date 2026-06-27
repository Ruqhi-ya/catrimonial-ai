package com.catrimonial.cat.controller;

import com.catrimonial.auth.security.UserPrincipal;
import com.catrimonial.cat.dto.CatRequest;
import com.catrimonial.cat.dto.CatResponse;
import com.catrimonial.cat.service.CatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/cats")
@RequiredArgsConstructor
@Tag(name = "Cat Management", description = "Cat profile CRUD operations")
public class CatController {

    private final CatService catService;

    @PostMapping
    @Operation(summary = "Create a new cat profile")
    public ResponseEntity<CatResponse> createCat(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody CatRequest request) {
        CatResponse response = catService.createCat(userPrincipal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{catId}")
    @Operation(summary = "Update a cat profile")
    public ResponseEntity<CatResponse> updateCat(
            @PathVariable UUID catId,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody CatRequest request) {
        CatResponse response = catService.updateCat(catId, userPrincipal.getId(), request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{catId}")
    @Operation(summary = "Delete a cat profile (soft delete)")
    public ResponseEntity<Map<String, String>> deleteCat(
            @PathVariable UUID catId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        catService.deleteCat(catId, userPrincipal.getId());
        return ResponseEntity.ok(Map.of("message", "Cat profile deleted successfully"));
    }

    @GetMapping("/{catId}")
    @Operation(summary = "Get a cat profile by ID")
    public ResponseEntity<CatResponse> getCat(@PathVariable UUID catId) {
        CatResponse response = catService.getCat(catId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-cats")
    @Operation(summary = "Get all cats for the authenticated user")
    public ResponseEntity<Page<CatResponse>> getMyCats(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<CatResponse> cats = catService.getMyCats(userPrincipal.getId(), pageable);
        return ResponseEntity.ok(cats);
    }

    @GetMapping("/public/search")
    @Operation(summary = "Search cats by filters")
    public ResponseEntity<Page<CatResponse>> searchCats(
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String color,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<CatResponse> cats = catService.searchCats(breed, gender, city, color, pageable);
        return ResponseEntity.ok(cats);
    }

    @GetMapping("/public/nearby")
    @Operation(summary = "Get nearby cats based on city")
    public ResponseEntity<Page<CatResponse>> getNearbyCats(
            @RequestParam String city,
            @RequestParam(required = false) UUID excludeId,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<CatResponse> cats = catService.getNearbyCats(city, excludeId, pageable);
        return ResponseEntity.ok(cats);
    }

    @GetMapping("/public/breeds")
    @Operation(summary = "Get all available cat breeds")
    public ResponseEntity<List<String>> getAllBreeds() {
        return ResponseEntity.ok(catService.getAllBreeds());
    }
}
