// @formatter:off
package com.everhomes.parking.invoice;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.parking.ParkingLot;
import com.everhomes.parking.ParkingProvider;
import com.everhomes.parking.ParkingRechargeOrder;
import com.everhomes.rest.parking.ParkingErrorCode;
import com.everhomes.rest.parking.ParkingLotDTO;
import com.everhomes.rest.parking.ParkingRechargeOrderDTO;
import com.everhomes.rest.parking.invoice.*;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/6/22 16:27
 */

@Component
public class InvoiceServiceImpl implements InvoiceService{
    @Autowired
    public ParkingProvider parkingProvider;
    @Autowired
    private ConfigurationProvider configProvider;
    @Override
    public ListNotInvoicedOrdersResponse listNotInvoicedOrders(ListNotInvoicedOrdersCommand cmd) {
        Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long pageAnchor = cmd.getPageAnchor()==null?0:cmd.getPageAnchor();
        List<ParkingRechargeOrder> list = parkingProvider.listParkingRechargeOrdersByUserId(cmd.getUserId(),cmd.getStartCreateTime(),cmd.getEndCreateTime(),pageSize,pageAnchor);
        Map<String,ParkingLot> lotsMap = new HashMap<String,ParkingLot>();
        ListNotInvoicedOrdersResponse response = new ListNotInvoicedOrdersResponse();
        if(list !=null && list.size()>0 ){
            response.setInvoicedOrderList(list.stream().map(r->{
                InvoicedOrderDTO dto = new InvoicedOrderDTO();
                dto.setAmount(r.getPrice());
                dto.setCreateTime(r.getCreateTime());
                dto.setOrderNo(r.getId());
                if(lotsMap.get(""+r.getParkingLotId()) == null){
                    ParkingLot lots = parkingProvider.findParkingLotById(r.getParkingLotId());
                    lotsMap.put(""+r.getParkingLotId(),lots);
                }
                dto.setParkingName(lotsMap.get(""+r.getParkingLotId()).getName());
                return dto;
            }).collect(Collectors.toList()));
            if(list.size()==pageSize){
                response.setNextPageAnchor(pageAnchor+1);
            }
        }
        response.setCount(parkingProvider.ParkingRechargeOrdersByUserId(cmd.getUserId(),cmd.getStartCreateTime(),cmd.getEndCreateTime()));
        return response;
    }

    @Override
    public void notifyOrderInvoiced(NotifyOrderInvoicedCommand cmd) {
        ParkingRechargeOrder rechargeOrder = parkingProvider.findParkingRechargeOrderById(cmd.getOrderNo());
        if(rechargeOrder==null){
            throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_SELF_DEFINE,
                    "not find orderno");
        }
        rechargeOrder.setInvoiceStatus(cmd.getStatus());
        rechargeOrder.setInvoiceCreateTime(new Timestamp(System.currentTimeMillis()));
        parkingProvider.updateParkingRechargeOrder(rechargeOrder);
    }

    @Override
    public GetPayeeIdByOrderNoResponse getPayeeIdByOrderNo(GetPayeeIdByOrderNoCommand cmd) {
        ParkingRechargeOrder rechargeOrder = parkingProvider.findParkingRechargeOrderById(cmd.getOrderNo());
        if(rechargeOrder==null){
            throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_SELF_DEFINE,
                    "not find orderno");
        }
        GetPayeeIdByOrderNoResponse response = new GetPayeeIdByOrderNoResponse();
        response.setPayeeId(rechargeOrder.getPayeeId());
        return response;
    }

    @Override
    public List<ParkingLotDTO> listAllParkingLots(ListAllParkingLotsCommand cmd) {
        List<ParkingLot> list = parkingProvider.listParkingLots(null, null);
        List<ParkingLotDTO> parkingLotList = list.stream().map(r -> ConvertHelper.convert(r, ParkingLotDTO.class)).collect(Collectors.toList());
        return parkingLotList;
    }
    
    @Override
    public ParkingRechargeOrderDTO parkingRechargeOrdersByOrderNo (long orderNo){
		return parkingProvider.parkingRechargeOrdersByOrderNo(orderNo);
    }
}
