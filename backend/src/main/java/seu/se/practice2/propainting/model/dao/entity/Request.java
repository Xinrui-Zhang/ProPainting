package seu.se.practice2.propainting.model.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request {
    @Id
    private Integer rid;

    private String type;

    private String name;

    private Integer uid;

    private String avatar;

    private Integer pid;

    private String content;

    private Integer result;

    private Date ctime;

    private Date etime;


}