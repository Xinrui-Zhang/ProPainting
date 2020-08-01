package seu.se.practice2.propainting.model.dto;

import com.github.pagehelper.Page;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import seu.se.practice2.propainting.error.ClientException;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult {
    private Long total = 1L;
    private Integer totalPage = 1;
    private List<Object> pageList = null;

    public static <E> PageResult wrap(List<E> _pageList) throws ClientException {
        return new PageResult().wrapForList(_pageList);
    }

    public <E> PageResult wrapForList(List<E> _pageList) throws ClientException {
        if (_pageList instanceof Page) {
            return wrapForPage((Page<E>) _pageList);
        } else {
            throw new ClientException("转换 PageResult 失败!");
        }
    }

    public <E> PageResult wrapForPage(Page<E> page) {
        total = page.getTotal();
        totalPage = page.getPages();
        pageList = (List<Object>) page;
        return this;
    }
}
