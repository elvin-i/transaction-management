package homework.bank.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import homework.bank.dtvo.dto.order.CreateOrderDTO;
import homework.bank.dtvo.dto.order.UpdateOrderDTO;
import homework.bank.dtvo.vo.order.OrderVO;
import homework.bank.service.OrderService;
import homework.bank.service.exception.ServiceException;
import homework.bank.service.exception.ServiceExceptionCodeEnums;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    /**
     * 创建交易订单
     */
    @Test
    public void testCreateOrder() throws Exception {

        String requestNo = UUID.randomUUID().toString();
        // Arrange
        CreateOrderDTO createOrderDTO = new CreateOrderDTO();
        createOrderDTO.setRequestNo(requestNo);
        createOrderDTO.setBusinessType("type");
        createOrderDTO.setPayerAccountNo("ACT001");
        createOrderDTO.setPayerAccountName("账户001");
        createOrderDTO.setPayerOrgCode("机构A");
        createOrderDTO.setPayeeAccountNo("ACT002");
        createOrderDTO.setPayeeAccountName("账户002");
        createOrderDTO.setPayeeOrgCode("机构B");
        createOrderDTO.setAmount(new BigDecimal("10"));
        createOrderDTO.setRemark("备注");
        createOrderDTO.setPostscript("附言");

        OrderVO vo = new OrderVO();
        vo.setRequestNo(requestNo);
        vo.setBusinessType("type");
        vo.setPayerAccountName("账户001");
        vo.setPayeeAccountName("账户002");
        vo.setAmount(new BigDecimal("10"));

        when(orderService.create(any(CreateOrderDTO.class))).thenReturn(vo);

        // Act & Assert
        mockMvc.perform(post("/api/web/1.0/order").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(createOrderDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.requestNo").value(requestNo))
                .andExpect(jsonPath("$.data.businessType").value("type"))
                .andExpect(jsonPath("$.data.payerAccountName").value("账户001"))
                .andExpect(jsonPath("$.data.payeeAccountName").value("账户002"))
                .andExpect(jsonPath("$.data.amount").value(10));
    }

    /**
     * 删除交易订单
     */
    @Test
    public void testDeleteOrder() throws Exception {
        // Arrange
        doNothing().when(orderService).delete(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/web/1.0/order/{id}", 1L)).andExpect(status().isOk());

        verify(orderService, times(1)).delete(1L);
    }

    /**
     * 更新交易订单
     */
    @Test
    public void testUpdateOrder() throws Exception {
        // Arrange
        UpdateOrderDTO updateOrderDTO = new UpdateOrderDTO();
        updateOrderDTO.setRemark("更新备注");
        updateOrderDTO.setAmount(new BigDecimal("0.01"));

        OrderVO vo = new OrderVO();
        vo.setId(1L);
        vo.setRemark("更新备注");

        when(orderService.update(eq(1L), any(UpdateOrderDTO.class))).thenReturn(vo);

        // Act & Assert
        mockMvc.perform(put("/api/web/1.0/order/{id}", 1L).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateOrderDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.remark").value("更新备注"));
    }

    /**
     * 查询交易订单 - 详情
     */
    @Test
    public void testGetOrder() throws Exception {
        // Arrange
        OrderVO vo = new OrderVO();
        vo.setId(1L);
        vo.setBusinessType("type");
        vo.setAmount(new BigDecimal("10"));

        when(orderService.get(1L)).thenReturn(vo);

        // Act & Assert
        mockMvc.perform(get("/api/web/1.0/order/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.businessType").value("type"))
                .andExpect(jsonPath("$.data.amount").value(10));
    }

    /**
     * 查询交易订单 - 分页
     */
    @Test
    public void testGetPageOrders() throws Exception {
        // Arrange
        OrderVO vo = new OrderVO();
        vo.setId(1L);
        vo.setBusinessType("type");
        vo.setAmount(new BigDecimal("10"));

        Page<OrderVO> page = new Page<>();
        page.setRecords(Collections.singletonList(vo));

        when(orderService.getPage(anyInt(), anyInt(), anyString(), anyString(), isNull())).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/api/web/1.0/order")
                        .param("pageNo", "0")
                        .param("pageSize", "10")
                        .param("startCreateTime", "2025-01-01 00:00:00")
                        .param("endCreateTime", "2025-08-28 23:59:59"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.records[0].id").value(1L))
                .andExpect(jsonPath("$.data.records[0].businessType").value("type"))
                .andExpect(jsonPath("$.data.records[0].amount").value(10));
    }

    //===============================================异常用例===========================================

    /**
     * 创建交易订单 - 字段值非法
     */
    @Test
    public void testCreateOrderValidationFail() throws Exception {
        // Arrange
        CreateOrderDTO invalidDTO = new CreateOrderDTO();
        // 无效金额
        invalidDTO.setAmount(BigDecimal.ZERO);

        // Act & Assert
        mockMvc.perform(post("/api/web/1.0/order").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    /**
     * 删除交易订单 - 不存在
     */
    @Test
    public void testDeleteOrderNotFound() throws Exception {
        // Arrange
        ServiceException serviceException = new ServiceException("订单不存在");
        serviceException.setCode(ServiceExceptionCodeEnums.ILLEGAL_ARGUMENT_EXCEPTION.getCode());
        doThrow(serviceException).when(orderService).delete(999L);

        // Act & Assert
        mockMvc.perform(delete("/api/web/1.0/order/{id}", 999L)).andExpect(status().isBadRequest()).andExpect(jsonPath("$.info").value("订单不存在"));
    }

    /**
     * 更新交易订单 - 传入非法参数
     */
    @Test
    public void testUpdateOrderValidationFail() throws Exception {
        // Arrange
        UpdateOrderDTO dto = new UpdateOrderDTO();
        dto.setAmount(BigDecimal.ZERO);

        // Act & Assert
        mockMvc.perform(put("/api/web/1.0/order/{id}", 1L).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    /**
     * 查询交易订单 - 不存在
     */
    @Test
    public void testGetOrderNotFound() throws Exception {
        // Arrange
        ServiceException serviceException = new ServiceException("订单不存在");
        serviceException.setCode(ServiceExceptionCodeEnums.ILLEGAL_ARGUMENT_EXCEPTION.getCode());
        when(orderService.get(999L)).thenThrow(serviceException);

        // Act & Assert
        mockMvc.perform(get("/api/web/1.0/order/{id}", 999L)) .andExpect(status().isBadRequest()).andExpect(jsonPath("$.info").value("订单不存在"));
    }

}
