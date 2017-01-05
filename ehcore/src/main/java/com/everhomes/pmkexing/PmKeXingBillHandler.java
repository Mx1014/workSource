package com.everhomes.pmkexing;

import com.everhomes.rest.pmkexing.*;
import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

/**
 * 正中会一碑物业缴费
 * Created by xq.tian on 2016/12/27.
 */
class PmKeXingBillHandler implements PmBillHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PmKeXingBillHandler.class);

    @Override
    public PmBillVendor[] initHandler() {
        return new PmBillVendor[]{PmBillVendor.ZZH_YI_BEI};
    }

    @Override
    public List<ListOrganizationsByPmAdminDTO> listOrganizationsByPmOwner() {
        return null;
    }

    @Override
    public ListPmKeXingBillsResponse listPmBills(ListPmKeXingBillsCommand cmd) {
        return null;
    }

    @Override
    public PmKeXingBillDTO getPmBill(GetPmKeXingBillCommand cmd) {
        return null;
    }

    private static class Bill {
        String contractNum;
        String fiName;
        private String billDate;
        private BigDecimal receivable;
        private BigDecimal actualmoney;
        private String isPay;
        String hasNextPag;

        private PmKeXingBillDTO toBillDTO() {
            PmKeXingBillDTO dto = new PmKeXingBillDTO();
            dto.setBillDate(billDate);
            dto.setReceivableAmount(receivable);
            dto.setUnpaidAmount(actualmoney);
            dto.setBillStatus(isPay);
            return dto;
        }

        @Override
        public String toString() {
            return StringHelper.toJsonString(this);
        }
    }

    @Override
    public PmKeXingBillStatDTO getPmBillStat(GetPmKeXingBillStatCommand cmd) {
        return null;
    }

    private static class BillStat {
        private BigDecimal arrearageMonthCount;
        private BigDecimal arrearageSum;
        BigDecimal alreadyMonthCount;
        BigDecimal alreadySum;

        private PmKeXingBillStatDTO toBillStatDTO() {
            PmKeXingBillStatDTO dto = new PmKeXingBillStatDTO();
            dto.setUnpaidAmount(arrearageSum);
            dto.setUnpaidMonth(arrearageMonthCount);
            return dto;
        }

        @Override
        public String toString() {
            return StringHelper.toJsonString(this);
        }
    }
}