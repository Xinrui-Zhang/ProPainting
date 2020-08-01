package seu.se.practice2.propainting.service;

import java.util.Map;

import org.springframework.validation.annotation.Validated;

import seu.se.practice2.propainting.error.ClientException;
import seu.se.practice2.propainting.error.ServiceException;
import seu.se.practice2.propainting.model.dto.PicInfoAndFile;
import seu.se.practice2.propainting.model.dto.TransferPic;
import seu.se.practice2.propainting.validation.NotNull;


@Validated
public interface PictureService {
    void updateLike(@NotNull Integer like,@NotNull Integer pid,
                    @NotNull String type,@NotNull Long uid)
            throws ServiceException,ClientException;
    // 底下3个接口由shuxinyue改动
    void addPicture(@NotNull PicInfoAndFile picInfoAndFile,String state, @NotNull Long uid)throws  ServiceException, ClientException;

    void deletePicture(@NotNull Integer pid, @NotNull Long uid)throws ServiceException,ClientException;

    void updatePicture(@NotNull PicInfoAndFile picInfoAndFile, @NotNull Long uid)throws ServiceException,ClientException;


    void updateVisibiltyP(@NotNull Integer pid,@NotNull String visibility)throws ServiceException,ClientException;

    Map getPicture(@NotNull Integer pid, @NotNull Long uid)throws  ServiceException,ClientException;
	Object getColorCard(@NotNull Integer pid)throws  ServiceException,ClientException;
	Object transferPic(@NotNull TransferPic transferPic) throws ClientException, ServiceException;
}