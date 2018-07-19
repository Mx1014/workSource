package com.everhomes.rest.pmtask;


/**
 * <ul>
 * <li>evalName: 评价项目</li>
 * <li>amount5: 评分为5数量</li>
 * <li>amount4: 评分为4数量</li>
 * <li>amount3: 评分为3数量</li>
 * <li>amount2: 评分为2数量</li>
 * <li>amount1: 评分为1数量</li>
 * <li>totalAmount: 评分总数量</li>
 * <li>evalAvg: 评分平均分</li>
 * </ul>
 */
public class PmTaskEvalStatDTO {

    private String evalName;
    private Integer amount5;
    private Integer amount4;
    private Integer amount3;
    private Integer amount2;
    private Integer amount1;
    private Integer totalAmount;
    private String evalAvg;

    public String getEvalName() {
        return evalName;
    }

    public void setEvalName(String evalName) {
        this.evalName = evalName;
    }

    public Integer getAmount5() {
        return amount5;
    }

    public void setAmount5(Integer amount5) {
        this.amount5 = amount5;
    }

    public Integer getAmount4() {
        return amount4;
    }

    public void setAmount4(Integer amount4) {
        this.amount4 = amount4;
    }

    public Integer getAmount3() {
        return amount3;
    }

    public void setAmount3(Integer amount3) {
        this.amount3 = amount3;
    }

    public Integer getAmount2() {
        return amount2;
    }

    public void setAmount2(Integer amount2) {
        this.amount2 = amount2;
    }

    public Integer getAmount1() {
        return amount1;
    }

    public void setAmount1(Integer amount1) {
        this.amount1 = amount1;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getEvalAvg() {
        return evalAvg;
    }

    public void setEvalAvg(String evalAvg) {
        this.evalAvg = evalAvg;
    }

    public PmTaskEvalStatDTO() {
        super();
        this.amount5 = 0;
        this.amount4 = 0;
        this.amount3 = 0;
        this.amount2 = 0;
        this.amount1 = 0;
        this.totalAmount = 0;
    }
}
