import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class City implements Serializable {
    public String id;
    public String city;
}
