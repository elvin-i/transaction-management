package homework.bank.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import homework.bank.common.Result;
import homework.bank.dtvo.dto.order.CreateOrderDTO;
import homework.bank.dtvo.dto.order.UpdateOrderDTO;
import homework.bank.dtvo.vo.order.OrderVO;
import homework.bank.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Controller
@RequestMapping("/api/web/1.0/order")
@Validated
public class OrderController {

    @Autowired
    OrderService orderService;

    /**
     * 创建交易订单
     */
    @PostMapping
    @ResponseBody
    public Result<OrderVO> create(@RequestBody @Valid CreateOrderDTO createOrderDTO) {
        return new Result.Builder().success(orderService.create(createOrderDTO));
    }


    /**
     * 删除交易订单
     */
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public void delete(@PathVariable(value = "id") @NotNull(message = "id不能为空")  Long id) {
        orderService.delete(id);
    }

    /**
     * 更新交易订单
     */
    @PutMapping(value = "/{id}")
    @ResponseBody
    public Result<OrderVO> update(@PathVariable(value = "id") @NotNull(message = "id不能为空") Long id, @RequestBody @Valid UpdateOrderDTO updateOrderDTO) {
        return new Result.Builder().success(orderService.update(id,updateOrderDTO));
    }


    /**
     * 查询交易订单 - 详情
     */
    @GetMapping(value = "/{id}")
    @ResponseBody
    public Result<OrderVO> get(@PathVariable(value = "id") @NotNull(message = "id不能为空")  Long id) {
        return new Result.Builder().success(orderService.get(id));
    }

    /**
     * 查询交易订单 - 分页
     */
    @GetMapping
    @ResponseBody
    public Result<Page<OrderVO>> getPage( @RequestParam(value = "pageNo", required = false, defaultValue = "0") int pageNo,
                                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                          @RequestParam(value = "startCreateTime", required = false) String startCreateTime,
                                          @RequestParam(value = "endCreateTime", required = false) String endCreateTime,
                                          @RequestParam(value = "requestNo", required = false) String requestNo
                                          ) {
        return new Result.Builder().success(orderService.getPage(pageNo,pageSize,startCreateTime,endCreateTime,requestNo));
    }

}
