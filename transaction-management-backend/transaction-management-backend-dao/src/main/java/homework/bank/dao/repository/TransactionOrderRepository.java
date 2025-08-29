package homework.bank.dao.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import homework.bank.dao.entity.TransactionOrder;
import homework.bank.dao.mapper.TransactionOrderMapper;
import org.springframework.stereotype.Service;

@Service
public class TransactionOrderRepository extends ServiceImpl<TransactionOrderMapper, TransactionOrder> {

}