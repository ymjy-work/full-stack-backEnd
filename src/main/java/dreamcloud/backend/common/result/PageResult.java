package dreamcloud.backend.common.result;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
@Data
public class PageResult<T> {
    private List<T> records;    // 当前页数据
    private long total;     // 总条数
    private int pageNum;    // 总页数
    private  int pageSize;  // 每页条数
}
