package root.demo.Dto;

import lombok.Data;
import root.demo.model.NacinPlacanja;

import java.util.List;

@Data
public class SellerDto {
    private Long id;
    private String email;
    private String generatedId;
    private List<NacinPlacanja> naciniPlacanja;
}
