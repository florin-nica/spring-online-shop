
package ro.msg.learning.shop.dto;

import lombok.Data;

import java.util.List;

@Data
public class DistanceMatrixDto {

    private String status;
    private List<String> originAddresses;
    private List<String> destinationAddresses;
    private List<RowDto> rows;

}
