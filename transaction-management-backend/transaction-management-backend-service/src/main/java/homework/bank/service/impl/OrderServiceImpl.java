package homework.bank.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import homework.bank.dao.entity.TransactionOrder;
import homework.bank.dao.repository.TransactionOrderRepository;
import homework.bank.dtvo.dto.order.CreateOrderDTO;
import homework.bank.dtvo.dto.order.UpdateOrderDTO;
import homework.bank.dtvo.vo.order.OrderVO;
import homework.bank.service.OrderService;
import homework.bank.service.exception.ServiceException;
import homework.bank.service.exception.ServiceExceptionCodeEnums;
import homework.bank.service.impl.converter.OrderConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    TransactionOrderRepository transactionOrderRepository;

    @Override
    public OrderVO create(CreateOrderDTO createOrderDTO) {
        // 1. 转换实体
        TransactionOrder transactionOrder = OrderConverter.fromCreateOrderDTO2Entity(createOrderDTO);
        // 2. 落库
        try {
            transactionOrderRepository.save(transactionOrder);
        }
        // 3. 幂等处理
        catch (DuplicateKeyException e) {
            return this.getByRequestNo(transactionOrder.getRequestNo());
        }
        // 4. 响应
        return this.getByRequestNo(transactionOrder.getRequestNo());
    }

    @Override
    public void delete(Long id) {
        boolean b = transactionOrderRepository.removeById(id);
        if (!b) {
            log.error("delete - {},id : {}", ServiceExceptionCodeEnums.DELETE_ORDER_WRONG_ORDER_NOT_EXIST.getInfo(), id);
            throw new ServiceException(ServiceExceptionCodeEnums.DELETE_ORDER_WRONG_ORDER_NOT_EXIST);
        }
    }

    @Override
    public OrderVO update(Long id, UpdateOrderDTO updateOrderDTO) {

        // 1. 构造更新条件
        LambdaUpdateWrapper<TransactionOrder> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(TransactionOrder::getId,id);

        if (StringUtils.isNotBlank(updateOrderDTO.getBusinessType())) {
            lambdaUpdateWrapper.eq(TransactionOrder::getBusinessType, updateOrderDTO.getBusinessType());
        }

        if (StringUtils.isNotBlank(updateOrderDTO.getPayerAccountNo())) {
            lambdaUpdateWrapper.eq(TransactionOrder::getPayerAccountNo, updateOrderDTO.getPayerAccountNo());
        }

        if (StringUtils.isNotBlank(updateOrderDTO.getPayerAccountName())) {
            lambdaUpdateWrapper.eq(TransactionOrder::getPayerAccountName, updateOrderDTO.getPayerAccountName());
        }

        if (StringUtils.isNotBlank(updateOrderDTO.getPayerOrgCode())) {
            lambdaUpdateWrapper.eq(TransactionOrder::getPayerOrgCode, updateOrderDTO.getPayerOrgCode());
        }

        if (StringUtils.isNotBlank(updateOrderDTO.getPayeeAccountNo())) {
            lambdaUpdateWrapper.eq(TransactionOrder::getPayeeAccountNo, updateOrderDTO.getPayeeAccountNo());
        }

        if (StringUtils.isNotBlank(updateOrderDTO.getPayeeAccountName())) {
            lambdaUpdateWrapper.eq(TransactionOrder::getPayeeAccountName, updateOrderDTO.getPayeeAccountName());
        }

        if (StringUtils.isNotBlank(updateOrderDTO.getPayeeOrgCode())) {
            lambdaUpdateWrapper.eq(TransactionOrder::getPayeeOrgCode, updateOrderDTO.getPayeeOrgCode());
        }

        if (updateOrderDTO.getAmount() != null) {
            lambdaUpdateWrapper.eq(TransactionOrder::getAmount, updateOrderDTO.getAmount());
        }

        if (StringUtils.isNotBlank(updateOrderDTO.getRemark())) {
            lambdaUpdateWrapper.eq(TransactionOrder::getRemark, updateOrderDTO.getRemark());
        }

        if (StringUtils.isNotBlank(updateOrderDTO.getPostscript())) {
            lambdaUpdateWrapper.eq(TransactionOrder::getPostscript, updateOrderDTO.getPostscript());
        }

        // 2. 执行更新
        boolean update = transactionOrderRepository.update(lambdaUpdateWrapper);

        // 3. 响应
        if (update) {
            return this.getById(id);
        }
        log.error("update - {},id : {}", ServiceExceptionCodeEnums.UPDATE_ORDER_WRONG_ORDER_NOT_EXIST_OR_NOT_CHANGE.getInfo(), id);
        throw new ServiceException(ServiceExceptionCodeEnums.UPDATE_ORDER_WRONG_ORDER_NOT_EXIST_OR_NOT_CHANGE);
    }


    @Override
    public OrderVO get(Long id) {
        return this.getById(id);
    }

    @Override
    public Page<OrderVO> getPage(int pageNo, int pageSize, String startCreateTime, String endCreateTime) {

        // 1. 构造分页对象
        Page<TransactionOrder> page = new Page<>(pageNo, pageSize);

        // 2. 构造查询条件
        LambdaQueryWrapper<TransactionOrder> queryWrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotBlank(startCreateTime)) {
            queryWrapper.ge(TransactionOrder::getGmtCreated, startCreateTime);
        }
        if (StringUtils.isNotBlank(endCreateTime)) {
            queryWrapper.le(TransactionOrder::getGmtCreated, endCreateTime);
        }
        queryWrapper.orderByDesc(TransactionOrder::getId);

        // 3. 执行分页查询
        Page<TransactionOrder> entityPage = transactionOrderRepository.page(page, queryWrapper);

        // 4. 转换为 VO 分页结果
        Page<OrderVO> voPage = new Page<>(pageNo, pageSize, entityPage.getTotal());
        List<OrderVO> voRecords = entityPage.getRecords().stream().map(OrderConverter::fromEntity2OrderVO).collect(Collectors.toList());
        voPage.setRecords(voRecords);

        return voPage;
    }

    private OrderVO getByRequestNo(String requestNo) {
        TransactionOrder one = transactionOrderRepository.getOne(new LambdaQueryWrapper<TransactionOrder>().eq(TransactionOrder::getRequestNo, requestNo));
        return OrderConverter.fromEntity2OrderVO(one);
    }

    private OrderVO getById(Long id) {
        TransactionOrder one = transactionOrderRepository.getById(id);
        if (one == null) {
            return new OrderVO();
        }
        return OrderConverter.fromEntity2OrderVO(one);
    }
}
