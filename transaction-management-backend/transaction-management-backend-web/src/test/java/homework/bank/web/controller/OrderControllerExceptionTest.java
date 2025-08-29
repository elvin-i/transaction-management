package homework.bank.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import homework.bank.dtvo.dto.order.CreateOrderDTO;
import homework.bank.dtvo.dto.order.UpdateOrderDTO;
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

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class OrderControllerExceptionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    /**
     * 查询不存在的订单
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

    /**
     * 删除不存在的订单
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
     * 更新订单时传入非法参数
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
     * 创建订单时缺失必填字段
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
}
