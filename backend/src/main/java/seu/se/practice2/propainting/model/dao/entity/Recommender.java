package seu.se.practice2.propainting.model.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recommender {
    @Id
    private Long uid;

    private Integer id;

    private String type;

    private Date time;
}
