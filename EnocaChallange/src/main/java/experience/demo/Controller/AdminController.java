package experience.demo.Controller;

import experience.demo.Dto.OrderDto;
import experience.demo.Dto.ProductDto;
import experience.demo.Dto.Request.CreateProductRequest;
import experience.demo.Dto.Request.UpdateProductRequest;
import experience.demo.Model.Product;
import experience.demo.Service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    @PostMapping("/create")
    public ResponseEntity<ProductDto> createProduct(@RequestBody CreateProductRequest createProductRequest){
        return ResponseEntity.ok(adminService.createProduct(createProductRequest));
    }
    @GetMapping("/getAllProduct")
    public ResponseEntity<List<ProductDto>> getAllProducts(){
        List<ProductDto> products = adminService.getAllProduct();
        return ResponseEntity.ok(products);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
        adminService.deleteProduct(id);
        return ResponseEntity.ok().body("Product deleted successfully");
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody UpdateProductRequest updateProductRequest){
        return ResponseEntity.ok(adminService.updateProduct(id, updateProductRequest));
    }
    @GetMapping("/orders")
    public ResponseEntity<List<OrderDto>> GetAllOrdersForCustomer(){
        List<OrderDto> orderDtoList = adminService.GetAllOrdersForCustomer();
        return ResponseEntity.ok(orderDtoList);
    }

}
