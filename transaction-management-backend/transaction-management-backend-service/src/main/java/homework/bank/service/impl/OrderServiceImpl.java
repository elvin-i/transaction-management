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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    TransactionOrderRepository transactionOrderRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "orderCache", allEntries = true)
    public OrderVO create(CreateOrderDTO createOrderDTO) {
        // 1. 转换实体
        TransactionOrder transactionOrder = OrderConverter.fromCreateOrderDTO2Entity(createOrderDTO);
        // 2. 落库
        try {
            transactionOrderRepository.save(transactionOrder);
        }
        // 3. 幂等处理
        catch (DuplicateKeyException e) {
            log.info("命中幂等逻辑,直接返回库里数据 requestNo:{}",createOrderDTO.getRequestNo());
            return this.getByRequestNo(transactionOrder.getRequestNo());
        }
        // 4. 响应
        return this.getByRequestNo(transactionOrder.getRequestNo());
    }

    @Override
    @CacheEvict(value = "orderCache", allEntries = true)
    public void delete(Long id) {
        log.info("查询详情-id:{}",id);
        boolean b = transactionOrderRepository.removeById(id);
        if (!b) {
            log.error("delete - {},id : {}", ServiceExceptionCodeEnums.DELETE_ORDER_WRONG_ORDER_NOT_EXIST.getInfo(), id);
            throw new ServiceException(ServiceExceptionCodeEnums.DELETE_ORDER_WRONG_ORDER_NOT_EXIST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "orderCache", allEntries = true)
    public OrderVO update(Long id, UpdateOrderDTO updateOrderDTO) {

        // 1. 查询原始订单
        TransactionOrder existing = transactionOrderRepository.getById(id);
        if (existing == null) {
            log.error("update - {}, id : {}",
                    ServiceExceptionCodeEnums.UPDATE_ORDER_WRONG_ORDER_NOT_EXIST_OR_NOT_CHANGE.getInfo(), id);
            throw new ServiceException(ServiceExceptionCodeEnums.UPDATE_ORDER_WRONG_ORDER_NOT_EXIST_OR_NOT_CHANGE);
        }

        // 2. 只更新非空字段
        if (StringUtils.isNotBlank(updateOrderDTO.getBusinessType())) {
            existing.setBusinessType(updateOrderDTO.getBusinessType());
        }
        if (StringUtils.isNotBlank(updateOrderDTO.getPayerAccountNo())) {
            existing.setPayerAccountNo(updateOrderDTO.getPayerAccountNo());
        }
        if (StringUtils.isNotBlank(updateOrderDTO.getPayerAccountName())) {
            existing.setPayerAccountName(updateOrderDTO.getPayerAccountName());
        }
        if (StringUtils.isNotBlank(updateOrderDTO.getPayerOrgCode())) {
            existing.setPayerOrgCode(updateOrderDTO.getPayerOrgCode());
        }
        if (StringUtils.isNotBlank(updateOrderDTO.getPayeeAccountNo())) {
            existing.setPayeeAccountNo(updateOrderDTO.getPayeeAccountNo());
        }
        if (StringUtils.isNotBlank(updateOrderDTO.getPayeeAccountName())) {
            existing.setPayeeAccountName(updateOrderDTO.getPayeeAccountName());
        }
        if (StringUtils.isNotBlank(updateOrderDTO.getPayeeOrgCode())) {
            existing.setPayeeOrgCode(updateOrderDTO.getPayeeOrgCode());
        }
        if (updateOrderDTO.getAmount() != null) {
            existing.setAmount(updateOrderDTO.getAmount());
        }
        if (StringUtils.isNotBlank(updateOrderDTO.getRemark())) {
            existing.setRemark(updateOrderDTO.getRemark());
        }
        if (StringUtils.isNotBlank(updateOrderDTO.getPostscript())) {
            existing.setPostscript(updateOrderDTO.getPostscript());
        }

        // 3. 执行更新
        boolean update = transactionOrderRepository.updateById(existing);

        // 3. 响应
        if (update) {
            return this.getById(id);
        }
        log.error("update - {},id : {}", ServiceExceptionCodeEnums.UPDATE_ORDER_WRONG_ORDER_NOT_EXIST_OR_NOT_CHANGE.getInfo(), id);
        throw new ServiceException(ServiceExceptionCodeEnums.UPDATE_ORDER_WRONG_ORDER_NOT_EXIST_OR_NOT_CHANGE);
    }


    @Override
    @Cacheable(value = "orderCache", key = "'transactionOrder:' + #id")
    public OrderVO get(Long id) {
        return this.getById(id);
    }

    @Override
    @Cacheable(value = "orderCache", key = "(#startCreateTime != null ? #startCreateTime : '') + (#endCreateTime != null ? #endCreateTime : '') + (#requestNo != null ? #requestNo : '') + '_' + #pageNo + '_' + #pageSize", unless = "#result == null")
    public Page<OrderVO> getPage(int pageNo, int pageSize, String startCreateTime, String endCreateTime, String requestNo) {

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
        if (StringUtils.isNotBlank(requestNo)) {
            queryWrapper.eq(TransactionOrder::getRequestNo, requestNo);
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
