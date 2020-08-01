package seu.se.practice2.propainting.service;

import java.util.List;
import java.util.Map;

import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;
import seu.se.practice2.propainting.error.ClientException;
import seu.se.practice2.propainting.error.ServiceException;
import seu.se.practice2.propainting.validation.NotNull;
import seu.se.practice2.propainting.model.dto.PageResult;
import org.hibernate.validator.constraints.Range;

@Validated
public interface CollectionService {

	void insertPictoCollect(
		@NotNull Integer pid,
		@NotNull Integer cid,
		@NotNull String type,
		@NotNull Long uid
	) throws ServiceException, ClientException;

	void deletePictoCollect(@NotNull Integer pid, @NotNull Integer cid, @NotNull String type)
		throws ServiceException, ClientException;

	void createCollection(@NotNull String title,@NotNull String intro,@NotNull String visibility, @NotNull Long uid)
		throws ServiceException, ClientException;

	void updateVisibiltyS(@NotNull Integer cid, @NotNull String visibility)
		throws ServiceException, ClientException;

	Object getCollection(@NotNull Long uid,@Range(min = 0) Integer pageIndex,
						 @Range(min = 0) Integer pageSize)
			throws ServiceException, ClientException;

	// List getPictureInfo(@NotNull Integer cid) throws ServiceException, ClientException;

	Map getOthersCollection(@NotNull Integer cid, @NotNull Long uid) throws ServiceException, ClientException;


	PageResult getPictureList(
		@NotNull Integer cid,
		@Range(min = 0) Integer pageIndex,
		@Range(min = 0) Integer pageSize)
		throws ServiceException, ClientException;

	PageResult getCollectionList(
		@NotNull Long uid,
		@Range(min = 0) Integer pageIndex,
		@Range(min = 0) Integer pageSize)
		throws ServiceException, ClientException;


}