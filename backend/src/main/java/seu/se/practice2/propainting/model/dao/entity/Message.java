package seu.se.practice2.propainting.model.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    private Integer mid;

    private Integer senid;

    private String senname;

    private String senavatar;

    private Integer rcvid;

    private String rcvname;

    private String rcvavatar;

    private String content;

    private Date stime;

    private String state;


}