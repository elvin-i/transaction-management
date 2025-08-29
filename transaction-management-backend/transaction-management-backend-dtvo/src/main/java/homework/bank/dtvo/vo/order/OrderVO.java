package homework.bank.dtvo.vo.order;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderVO {

    private Long id;

    /**
     * 业务请求流水号,用于幂等
     */
    private String requestNo;

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
    private BigDecimal amount;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 附言
     */
    private String postscript;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreated;

    /**
     * 更新时间
     */
    private LocalDateTime gmtUpdated;

}
