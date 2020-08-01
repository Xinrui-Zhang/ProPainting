package seu.se.practice2.propainting.model.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Likemap {
    @Id
    private Integer uid;

    private String type;

    private Integer id;


}