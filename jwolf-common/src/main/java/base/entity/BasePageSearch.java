package base.entity;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Getter;
import lombok.Setter;

/**
 * @author jwolf
 * @Date 2021-10-29 22:24
 */

@Getter
@Setter
public class BasePageSearch {
    private Integer pageNum = 1;
    private Integer pageSize = 20;
    private String keyword;

    public Page getPage() {
        return new Page(pageNum, pageSize);
    }

}
