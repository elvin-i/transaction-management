package homework.bank.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import homework.bank.dao.entity.TransactionOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TransactionOrderMapper extends BaseMapper<TransactionOrder> {
}
