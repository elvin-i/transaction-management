package homework.bank.dtvo.dto.order;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateOrderDTO {

    /**
     * 业务请求流水号,用于幂等
     */
    @NotBlank(message = "业务请求流水号 不能为空")
    private String requestNo;

    /**
     * 业务类型
     */
    @NotBlank(message = "业务类型 不能为空")
    private String businessType;

    /**
     * 出款方账户编号
     */
    @NotBlank(message = "出款方账户编号 不能为空")
    private String payerAccountNo;

    /**
     * 出款方账户名称
     */
    @NotBlank(message = "出款方账户名称 不能为空")
    private String payerAccountName;

    /**
     * 出款方机构编码
     */
    @NotBlank(message = "出款方机构编码 不能为空")
    private String payerOrgCode;

    /**
     * 收款方账户编号
     */
    @NotBlank(message = "收款方账户编号 不能为空")
    private String payeeAccountNo;

    /**
     * 收款方账户名称
     */
    @NotBlank(message = "收款方账户名称 不能为空")
    private String payeeAccountName;

    /**
     * 收款方机构编码
     */
    @NotBlank(message = "收款方机构编码 不能为空")
    private String payeeOrgCode;

    /**
     * 交易金额
     */
    @NotNull(message = " 不能为空")
    @DecimalMin(value = "0.01", message = "金额必须大于等于0.01")
    private BigDecimal amount;

    /**
     * 备注
     */
    private String remark;

    /**
     * 附言
     */
    private String postscript;

}
