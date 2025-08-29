package homework.bank.dtvo.dto.order;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateOrderDTO {


    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 出款方账户编号
     */
    private String payerAccountNo;

    /**
     * 出款方账户名称
     */
    private String payerAccountName;

    /**
     * 出款方机构编码
     */
    private String payerOrgCode;

    /**
     * 收款方账户编号
     */
    private String payeeAccountNo;

    /**
     * 收款方账户名称
     */
    private String payeeAccountName;

    /**
     * 收款方机构编码
     */
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
