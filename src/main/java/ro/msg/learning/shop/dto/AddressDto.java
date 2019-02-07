package ro.msg.learning.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddressDto {

    private String country;
    private String city;
    private String county;
    private String street;
}
