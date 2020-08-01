package seu.se.practice2.propainting.service;

import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

import seu.se.practice2.propainting.error.ClientException;
import seu.se.practice2.propainting.error.ServerException;
import seu.se.practice2.propainting.model.dto.PageResult;

@Validated
public interface SearchService {
    PageResult getMasterOutline(
        String q, String genre, String tech,String style,
        @Range(min = 0) Integer pageIndex,
        @Range(min = 0) Integer pageSize
    ) throws ServerException, ClientException;

    PageResult getRandomMasterOutline(
        String genre, String tech,String style,
        @Range(min = 0) Integer pageIndex,
        @Range(min = 0) Integer pageSize
    ) throws ServerException, ClientException;

    PageResult getReferOutline(
        String q,
        @Range(min = 0) Integer pageIndex,
        @Range(min = 0) Integer pageSize
    ) throws ServerException, ClientException;


    Object getThreeDModel(
        String q,
        @Range(min = 0) Integer pageIndex,
        @Range(min = 0) Integer pageSize,
        Integer period,
        Integer minFaces,
        Integer maxFaces,
        Integer pbr,
        Boolean animated,
        Integer sort
    ) throws ServerException, ClientException;

    PageResult getCollectionOutline(
        String q,
        @Range(min = 0) Integer pageIndex,
        @Range(min = 0) Integer pageSize
    ) throws ServerException, ClientException;

    Object getPictureDetail(Integer pid, Long uid
    ) throws ServerException, ClientException;

    Object getCollectionDetail(Integer cid, Long uid
    ) throws ServerException, ClientException; 

}