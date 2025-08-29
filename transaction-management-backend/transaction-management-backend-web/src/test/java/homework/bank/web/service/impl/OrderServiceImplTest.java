package homework.bank.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import homework.bank.dao.entity.TransactionOrder;
import homework.bank.dao.repository.TransactionOrderRepository;
import homework.bank.dtvo.dto.order.CreateOrderDTO;
import homework.bank.dtvo.dto.order.UpdateOrderDTO;
import homework.bank.dtvo.vo.order.OrderVO;
import homework.bank.service.exception.ServiceException;
import homework.bank.service.exception.ServiceExceptionCodeEnums;
import homework.bank.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceImplTest {

    @Mock
    private TransactionOrderRepository transactionOrderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * 创建交易订单
     */
    @Test
    void testCreateOrderSuccess() {
        // Arrange
        CreateOrderDTO dto = new CreateOrderDTO();
        dto.setRequestNo("REQ123");
        dto.setBusinessType("TYPE");
        dto.setAmount(BigDecimal.TEN);


        TransactionOrder transactionOrder = new TransactionOrder();
        transactionOrder.setId(1L);
        transactionOrder.setRequestNo("REQ123");

        when(transactionOrderRepository.save(any(TransactionOrder.class))).thenReturn(true);
        when(transactionOrderRepository.getOne(any(Wrapper.class))).thenReturn(transactionOrder);

        // Act
        OrderVO result = orderService.create(dto);

        // Assert
        assertNotNull(result);
        assertEquals("REQ123", result.getRequestNo());

        verify(transactionOrderRepository, times(1)).save(any(TransactionOrder.class));
    }

    /**
     * 删除交易订单
     */
    @Test
    void testDeleteOrderSuccess() {
        // Arrange
        Long id = 1L;

        when(transactionOrderRepository.removeById(id)).thenReturn(true);

        // Act
        orderService.delete(id);

        // Assert
        verify(transactionOrderRepository, times(1)).removeById(id);
    }

    /**
     * 更新交易订单
     */
    @Test
    void testUpdateOrderSuccess() {
        // Arrange
        Long id = 1L;
        UpdateOrderDTO dto = new UpdateOrderDTO();
        dto.setRemark("updated remark");

        TransactionOrder transactionOrder = new TransactionOrder();
        transactionOrder.setId(1L);
        transactionOrder.setRemark("updated remark");

        when(transactionOrderRepository.update(any(Wrapper.class))).thenReturn(true);
        when(transactionOrderRepository.getById(any(Long.class))).thenReturn(transactionOrder);

        // Act
        OrderVO result = orderService.update(id, dto);

        // Assert
        assertNotNull(result);
        assertEquals("updated remark", result.getRemark());
    }

    /**
     * 查询交易订单 - 详情
     */
    @Test
    void testGetOrderSuccess() {
        // Arrange
        Long id = 1L;
        TransactionOrder order = new TransactionOrder();
        order.setId(id);
        order.setRequestNo("REQ123");

        when(transactionOrderRepository.getById(id)).thenReturn(order);

        // Act
        OrderVO result = orderService.get(id);

        // Assert
        assertNotNull(result);
        assertEquals("REQ123", result.getRequestNo());
        verify(transactionOrderRepository, times(1)).getById(id);
    }

    /**
     * 查询交易订单 - 详情缓存
     */
    @Test
    public void testGetOrderSuccessCache() throws Exception {

        // Act & Assert
        // 相同id查询2次看是否走缓存
        orderService.get(1L);
        orderService.get(1L);
        verify(transactionOrderRepository, times(1)).getById(1L);
    }

    /**
     * 查询交易订单 - 分页
     */
    @Test
    void testGetPageSuccess() {
        // Arrange
        int pageNo = 1;
        int pageSize = 10;
        Page<TransactionOrder> page = new Page<>(pageNo, pageSize);
        page.setRecords(List.of(new TransactionOrder()));

        when(transactionOrderRepository.page(any(Page.class), any())).thenReturn(page);

        // Act
        Page<OrderVO> result = orderService.getPage(pageNo, pageSize, null, null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getRecords().size());
        verify(transactionOrderRepository, times(1)).page(any(Page.class), any());
    }

    // ====================================异常case==========================================

    /**
     * 删除交易订单 - id不存在
     */
    @Test
    void testDeleteOrderNotFound() {
        // Arrange
        Long id = 1L;
        when(transactionOrderRepository.removeById(id)).thenReturn(false);

        // Act & Assert
        assertThrows(ServiceException.class, () -> orderService.delete(id));
        verify(transactionOrderRepository, times(1)).removeById(id);
    }

    /**
     * 更新交易订单 - id不存在
     */
    @Test
    void testUpdateOrderNotFound() {
        // Arrange
        Long id = 1L;
        UpdateOrderDTO dto = new UpdateOrderDTO();
        dto.setRemark("new remark");

        when(transactionOrderRepository.update(any(Wrapper.class))).thenReturn(false);

        // Act & Assert
        ServiceException ex = assertThrows(ServiceException.class,() -> orderService.update(id, dto));
        assertEquals(ServiceExceptionCodeEnums.UPDATE_ORDER_WRONG_ORDER_NOT_EXIST_OR_NOT_CHANGE.getInfo(), ex.getInfo());
        verify(transactionOrderRepository, times(1)).update(any(Wrapper.class));
    }

    /**
     * 查询交易订单 - 详情 - 不存在
     */
    @Test
    void testGetOrderNotFound() {
        // Arrange
        Long id = 1L;

        when(transactionOrderRepository.getById(id)).thenReturn(null);

        // Act
        OrderVO result = orderService.get(id);

        // Assert
        assertEquals(result,new OrderVO());
        verify(transactionOrderRepository, times(1)).getById(id);
    }

}
