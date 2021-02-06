import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Builder
@Data
@ToString
public class Emp implements Serializable {
    public String id;
    public String name;
    public Integer base;
    public String aaa;
}
