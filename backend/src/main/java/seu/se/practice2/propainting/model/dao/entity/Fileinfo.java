package seu.se.practice2.propainting.model.dao.entity;

import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Fileinfo {
    @Id
    private Integer pid;

    private String url;

    private String resolution;

    private String size;

    private String format;
}