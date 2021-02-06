import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@Builder
public class Result implements Serializable {

    public String id;
    public String city;
    public String bonus;


}
