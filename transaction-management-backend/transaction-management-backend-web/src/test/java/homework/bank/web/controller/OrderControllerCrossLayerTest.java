package homework.bank.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import homework.bank.dao.repository.TransactionOrderRepository;
import homework.bank.dtvo.dto.order.CreateOrderDTO;
import homework.bank.dtvo.vo.order.OrderVO;
import homework.bank.service.OrderService;
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
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class OrderControllerCrossLayerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 创建交易订单 - 测试幂等
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

        // Act & Assert
        // 相同请求流水号请求两次,返回第一次订单数据
        mockMvc.perform(post("/api/web/1.0/order").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(createOrderDTO)));
        mockMvc.perform(post("/api/web/1.0/order").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(createOrderDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.requestNo").value(requestNo))
                .andExpect(jsonPath("$.data.businessType").value("type"))
                .andExpect(jsonPath("$.data.payerAccountName").value("账户001"))
                .andExpect(jsonPath("$.data.payeeAccountName").value("账户002"))
                .andExpect(jsonPath("$.data.amount").value(10));
    }

    /**
     * 查询交易订单 - 测试缓存
     */
    @Test
    public void testGetOrder() throws Exception {

        // Act & Assert
        mockMvc.perform(get("/api/web/1.0/order/{id}", 1L));
        mockMvc.perform(get("/api/web/1.0/order/{id}", 1L));
    }

}
