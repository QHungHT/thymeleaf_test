package qh.synji.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class ProductEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(name = "product_name", length = 200, nullable = false)
    private String productName;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)  // Khóa ngoại trỏ về CategoryEntity
    private CategoryEntity category;
}
