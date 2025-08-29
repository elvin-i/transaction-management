package homework.bank.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import homework.bank.dtvo.dto.order.CreateOrderDTO;
import homework.bank.dtvo.dto.order.UpdateOrderDTO;
import homework.bank.dtvo.vo.order.OrderVO;

public interface OrderService {

    OrderVO create(CreateOrderDTO createOrderDTO);

    OrderVO update(Long id, UpdateOrderDTO updateOrderDTO);

    void delete(Long id);

    OrderVO get(Long id);

    Page<OrderVO> getPage(int pageNo, int pageSize, String startCreateTime, String endCreateTime, String requestNo);
}
