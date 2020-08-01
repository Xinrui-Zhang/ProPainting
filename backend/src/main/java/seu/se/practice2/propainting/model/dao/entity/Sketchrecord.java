package seu.se.practice2.propainting.model.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sketchrecord {
    @Id
    private Integer uid;

    private Integer pid;

    private String purl;

    private Date ftime;


}