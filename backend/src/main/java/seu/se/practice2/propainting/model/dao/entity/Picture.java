package seu.se.practice2.propainting.model.dao.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Picture {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer pid;

    private String type;

    private String titlezh;

    private String title;

    private String url;

    private Integer width;

    private Integer height;

    private String artistzh;

    private String artist;

    private Integer aid;

    private String avatar;

    private String genre;

    private String tech;

    private String style;

    private String introzh;

    private String intro;

    private String state;

    private String visibility;

    private String tag;

    private String refer;

    @Column(name = "`like`")
    private Integer like;

    private String colors;

}