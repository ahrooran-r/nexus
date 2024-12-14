package org.ahroo.nexus.controller;

import lombok.RequiredArgsConstructor;
import org.ahroo.nexus.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Ideally this should be an admin role. But I'm not complicating this setup further.
     */
    @GetMapping("/vacuum")
    public ResponseEntity<Integer> userProfile() {
        var result = categoryService.vacuum();
        return ResponseEntity.ok(result);
    }
}
