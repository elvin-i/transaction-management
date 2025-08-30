package homework.bank.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import homework.bank.dao.entity.TransactionOrder;
import homework.bank.dao.repository.TransactionOrderRepository;
import homework.bank.dtvo.dto.order.CreateOrderDTO;
import homework.bank.dtvo.dto.order.UpdateOrderDTO;
import homework.bank.dtvo.vo.order.OrderVO;
import homework.bank.service.exception.ServiceException;
import homework.bank.service.exception.ServiceExceptionCodeEnums;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceImplTest {

    @Mock
    private TransactionOrderRepository transactionOrderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    // ========================== Create ==========================
    @Test
    public void testCreateOrderSuccess() {
        CreateOrderDTO dto = new CreateOrderDTO();
        dto.setRequestNo("REQ123");
        dto.setBusinessType("TYPE");
        dto.setAmount(BigDecimal.TEN);

        TransactionOrder savedEntity = new TransactionOrder();
        savedEntity.setId(1L);
        savedEntity.setRequestNo(dto.getRequestNo());
        savedEntity.setBusinessType(dto.getBusinessType());
        savedEntity.setAmount(dto.getAmount());

        when(transactionOrderRepository.save(any(TransactionOrder.class))).thenReturn(true);
        when(transactionOrderRepository.getOne(any())).thenReturn(savedEntity);

        OrderVO vo = orderService.create(dto);

        assertNotNull(vo);
        assertEquals("REQ123", vo.getRequestNo());
        assertEquals("TYPE", vo.getBusinessType());
        assertEquals(BigDecimal.TEN, vo.getAmount());
        verify(transactionOrderRepository, times(1)).save(any(TransactionOrder.class));
    }

    // ========================== Update ==========================
    @Test
    public void testUpdateOrderSuccess() {
        Long id = 1L;
        UpdateOrderDTO dto = new UpdateOrderDTO();
        dto.setRemark("updated");

        TransactionOrder entity = new TransactionOrder();
        entity.setId(id);
        entity.setRemark("updated");

        when(transactionOrderRepository.getById(id)).thenReturn(entity);
        when(transactionOrderRepository.updateById(any(TransactionOrder.class))).thenReturn(true);

        OrderVO vo = orderService.update(id, dto);

        assertNotNull(vo);
        assertEquals("updated", vo.getRemark());
    }

    @Test
    public void testUpdateOrderNotFound() {
        Long id = 999L;
        UpdateOrderDTO dto = new UpdateOrderDTO();
        dto.setRemark("updated");

        when(transactionOrderRepository.getById(id)).thenReturn(null);

        try {
            orderService.update(id, dto);
            fail("Expected ServiceException");
        } catch (ServiceException ex) {
            assertEquals(ServiceExceptionCodeEnums.UPDATE_ORDER_WRONG_ORDER_NOT_EXIST_OR_NOT_CHANGE.getInfo(), ex.getInfo());
        }
    }

    @Test
    public void testUpdateOrderUpdateFail() {
        Long id = 1L;
        UpdateOrderDTO dto = new UpdateOrderDTO();
        dto.setRemark("remark");

        TransactionOrder entity = new TransactionOrder();
        entity.setId(id);

        when(transactionOrderRepository.getById(id)).thenReturn(entity);
        when(transactionOrderRepository.updateById(any(TransactionOrder.class))).thenReturn(false);

        try {
            orderService.update(id, dto);
            fail("Expected ServiceException");
        } catch (ServiceException ex) {
            assertEquals(ServiceExceptionCodeEnums.UPDATE_ORDER_WRONG_ORDER_NOT_EXIST_OR_NOT_CHANGE.getInfo(), ex.getInfo());
        }
    }

    // ========================== Get ==========================
    @Test
    public void testGetOrderSuccess() {
        Long id = 1L;
        TransactionOrder entity = new TransactionOrder();
        entity.setId(id);
        entity.setAmount(BigDecimal.TEN);

        when(transactionOrderRepository.getById(id)).thenReturn(entity);

        OrderVO vo = orderService.get(id);
        assertNotNull(vo);
        assertEquals(BigDecimal.TEN, vo.getAmount());
    }

    @Test
    public void testGetOrderNotFound() {
        Long id = 999L;
        when(transactionOrderRepository.getById(id)).thenReturn(null);

        OrderVO vo = orderService.get(id);
        assertEquals(new OrderVO(), vo);
    }

    // ========================== Delete ==========================
    @Test
    public void testDeleteOrderSuccess() {
        Long id = 1L;
        when(transactionOrderRepository.removeById(id)).thenReturn(true);

        orderService.delete(id);

        verify(transactionOrderRepository, times(1)).removeById(id);
    }

    @Test
    public void testDeleteOrderNotFound() {
        Long id = 999L;
        when(transactionOrderRepository.removeById(id)).thenReturn(false);

        try {
            orderService.delete(id);
            fail("Expected ServiceException");
        } catch (ServiceException ex) {
            assertEquals(ServiceExceptionCodeEnums.DELETE_ORDER_WRONG_ORDER_NOT_EXIST.getInfo(), ex.getInfo());
        }
    }

    // ========================== Page ==========================
    @Test
    public void testGetPageSuccess() {
        int pageNo = 1;
        int pageSize = 10;

        Page<TransactionOrder> page = new Page<>(pageNo, pageSize);
        TransactionOrder entity = new TransactionOrder();
        entity.setId(1L);
        entity.setAmount(BigDecimal.TEN);
        page.setRecords(Collections.singletonList(entity));

        when(transactionOrderRepository.page(any(Page.class), any(Wrapper.class))).thenReturn(page);

        Page<OrderVO> result = orderService.getPage(pageNo, pageSize, null, null, null);

        assertNotNull(result);
        assertEquals(1, result.getRecords().size());
        assertEquals(BigDecimal.TEN, result.getRecords().get(0).getAmount());
    }

    @Test
    public void testGetPageWithTimeRange() {
        int pageNo = 1;
        int pageSize = 10;

        Page<TransactionOrder> page = new Page<>(pageNo, pageSize);
        TransactionOrder entity = new TransactionOrder();
        entity.setId(1L);
        page.setRecords(Collections.singletonList(entity));

        when(transactionOrderRepository.page(any(Page.class), any(Wrapper.class))).thenReturn(page);

        Page<OrderVO> result = orderService.getPage(pageNo, pageSize, "2025-01-01", "2025-01-31", "REQ123");

        assertNotNull(result);
        assertEquals(1, result.getRecords().size());
    }
}
