package qh.synji.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryModel {
    private Long categoryId;
    private String name;
    private boolean isEdit;
    
}
