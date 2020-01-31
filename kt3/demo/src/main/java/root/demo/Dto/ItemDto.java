package root.demo.Dto;

import lombok.Data;

@Data
public class ItemDto {
    private String name;
    private Long izdanje;
    private String generatedId;
    private Long amount;
    private String sellerEmail;
}
