package com.geekbrains.spring.mvc.controllers;

import com.geekbrains.spring.mvc.model.Product;
import com.geekbrains.spring.mvc.services.ProductsService;
import com.geekbrains.spring.mvc.repositories.specifications.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// root: http://localhost:8189/app/products

@Controller
@RequestMapping("/products")
public class ProductsController {
    private ProductsService productsService;

    @Autowired
    public ProductsController(ProductsService studentsService) {
        this.productsService = studentsService;
    }

    @GetMapping
    public String showAllStudents(Model model,
        @RequestParam(name = "p", defaultValue = "1") Integer pageNumber,
        @RequestParam(name = "min_cost", required = false) Integer minCost,
        @RequestParam(name = "max_cost", required = false) Integer maxCost) {
        Specification<Product> spec = Specification.where(null);
        if (minCost != null) {
            spec = spec.and(ProductSpecifications.costGEThan(minCost));
        }
        if (maxCost != null) {
            spec = spec.and(ProductSpecifications.costLEThan(maxCost));
        }

        List<Product> products = productsService.findAll(spec, pageNumber).getContent();
        model.addAttribute("products", products);
        return "all_products";
    }

    @GetMapping("/add")
    public String showAddForm() {
        return "add_product_form";
    }

    @PostMapping("/add")
    public String saveNewProduct(@ModelAttribute Product newProduct) {
        productsService.saveOrUpdateProduct(newProduct);
        return "redirect:/products/";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productsService.findById(id));
        return "edit_product_form";
    }

    @PostMapping("/edit")
    public String modifyStudent(@ModelAttribute Product modifiedProduct) {
        productsService.saveOrUpdateProduct(modifiedProduct);
        return "redirect:/products/";
    }

    @GetMapping("/find_by_min_cost")
    @ResponseBody
    public List<Product> findProductsByMinCost(@RequestParam int cost) {
        return productsService.findByMinCost(cost);
    }

    @GetMapping("/find_by_max_cost")
    @ResponseBody
    public List<Product> findProductsByMaxCost(@RequestParam int cost) {
        return productsService.findByMaxCost(cost);
    }

    @GetMapping("/find_by_min_cost_or_max_cost")
    @ResponseBody
    public List<Product> findProductsByMinCostOrMaxCost(@RequestParam int minCost, int maxCost) {
        return productsService.findByCostBetween(minCost, maxCost);
    }

}
