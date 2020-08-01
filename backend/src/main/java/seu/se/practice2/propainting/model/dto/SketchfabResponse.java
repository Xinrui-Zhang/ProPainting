package seu.se.practice2.propainting.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SketchfabResponse {
    private String name = null;
    private String thumbnail = null;
    private String embedUrl = null;
    private Integer viewCount = null;
    private Integer likeCount = null;
    private Integer faceCount = null;
}