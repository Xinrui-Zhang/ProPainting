package seu.se.practice2.propainting.model.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "collection", schema = "propainting_dev", catalog = "")
public class Collect {
    @Id
    private Integer cid;

    private String title;

    private Integer uid;

    private String name;

    private String avatar;

    private String intro;

    private Date ctime;

    private Date etime;

    private String state;

    private String visibility;


}