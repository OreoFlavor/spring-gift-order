package gift.product.controller.view;

import gift.product.domain.Product;
import gift.product.dto.ProductPatchRequestDto;
import gift.product.dto.ProductSaveRequestDto;
import gift.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/api/admin/product")
public class ProductAdminViewController {
    private final ProductService productService;

    public ProductAdminViewController(ProductService productService) {
        this.productService = productService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
    }

    @GetMapping("/list")
    public String findAll(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/page")
    public String findAll(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            Model model
    ) {
        Page<Product> products = productService.findAllByPage(pageable);
        model.addAttribute("products", products.getContent());
        model.addAttribute("currentPage", pageable.getPageNumber());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("size", pageable.getPageSize());
        return "productsPage";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("productSaveRequestDto", new ProductSaveRequestDto());
        return "productAddForm";
    }

    @PostMapping("/add")
    public String createProduct(@Valid @ModelAttribute ProductSaveRequestDto productSaveRequestDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "productAddForm";
        }
        productService.createProduct(productSaveRequestDto);
        redirectAttributes.addAttribute("page", 0);
        redirectAttributes.addAttribute("size", 10);
        redirectAttributes.addAttribute("sortBy", "id");
        redirectAttributes.addAttribute("sortOrder", "asc");
        return "redirect:/api/admin/product/page";
    }

    @GetMapping("/{id}/update")
    public String updateForm(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        ProductPatchRequestDto productPatchRequestDto = new ProductPatchRequestDto(product);
        model.addAttribute("productPatchRequestDto", productPatchRequestDto);
        return "productUpdateForm";
    }

    @PatchMapping("/{id}/update")
    public String updateProduct(@PathVariable Long id, @Valid @ModelAttribute ProductPatchRequestDto productPatchRequestDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "productUpdateForm";
        }
        productService.updateProduct(id, productPatchRequestDto);
        redirectAttributes.addAttribute("page", 0);
        redirectAttributes.addAttribute("size", 10);
        redirectAttributes.addAttribute("sortBy", "id");
        redirectAttributes.addAttribute("sortOrder", "asc");
        return "redirect:/api/admin/product/page";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteById(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productService.deleteProduct(id);

        redirectAttributes.addAttribute("page", 0);
        redirectAttributes.addAttribute("size", 10);
        redirectAttributes.addAttribute("sortBy", "id");
        redirectAttributes.addAttribute("sortOrder", "asc");
        return "redirect:/api/admin/product/page";
    }
}
