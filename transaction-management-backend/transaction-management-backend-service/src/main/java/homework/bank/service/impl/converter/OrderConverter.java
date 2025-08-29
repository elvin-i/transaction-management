package homework.bank.service.impl.converter;

import homework.bank.dao.entity.TransactionOrder;
import homework.bank.dtvo.dto.order.CreateOrderDTO;
import homework.bank.dtvo.vo.order.OrderVO;

public class OrderConverter {
    public static TransactionOrder fromCreateOrderDTO2Entity(CreateOrderDTO createOrderDTO) {

        TransactionOrder transactionOrder = new TransactionOrder();

        transactionOrder.setRequestNo(createOrderDTO.getRequestNo());
        transactionOrder.setBusinessType(createOrderDTO.getBusinessType());
        transactionOrder.setPayerAccountNo(createOrderDTO.getPayerAccountNo());
        transactionOrder.setPayerAccountName(createOrderDTO.getPayerAccountName());
        transactionOrder.setPayerOrgCode(createOrderDTO.getPayerOrgCode());

        transactionOrder.setPayeeAccountNo(createOrderDTO.getPayeeAccountNo());
        transactionOrder.setPayeeAccountName(createOrderDTO.getPayeeAccountName());
        transactionOrder.setPayeeOrgCode(createOrderDTO.getPayeeOrgCode());
        transactionOrder.setAmount(createOrderDTO.getAmount());
        transactionOrder.setRemark(createOrderDTO.getRemark());

        transactionOrder.setPostscript(createOrderDTO.getPostscript());

        return transactionOrder;
    }

    public static OrderVO fromEntity2OrderVO(TransactionOrder transactionOrder) {
        OrderVO orderVO = new OrderVO();

        orderVO.setId(transactionOrder.getId());

        orderVO.setRequestNo(transactionOrder.getRequestNo());
        orderVO.setBusinessType(transactionOrder.getBusinessType());
        orderVO.setPayerAccountNo(transactionOrder.getPayerAccountNo());
        orderVO.setPayerAccountName(transactionOrder.getPayerAccountName());
        orderVO.setPayerOrgCode(transactionOrder.getPayerOrgCode());

        orderVO.setPayeeAccountNo(transactionOrder.getPayeeAccountNo());
        orderVO.setPayeeAccountName(transactionOrder.getPayeeAccountName());
        orderVO.setPayeeOrgCode(transactionOrder.getPayeeOrgCode());
        orderVO.setAmount(transactionOrder.getAmount());
        orderVO.setRemark(transactionOrder.getRemark());

        orderVO.setPostscript(transactionOrder.getPostscript());
        orderVO.setStatus(transactionOrder.getStatus());
        orderVO.setGmtCreated(transactionOrder.getGmtCreated());
        orderVO.setGmtUpdated(transactionOrder.getGmtUpdated());

        return orderVO;
    }
}
