package com.everhomes.rentalv2;

/**
 * @author sw on 2018/1/9.
 */
public interface RentalMessageHandler {
    String RENTAL_MESSAGE_HANDLER_PREFIX = "RentalMessageHandler-";
    String DEFAULT = "default";

    /**
     * 用户取消订单发送消息
     * @param rentalBill
     */
    void cancelOrderSendMessage(RentalOrder rentalBill);

    /**
     * 新增订单 发送消息
     * @param rentalBill
     */
    void addOrderSendMessage(RentalOrder rentalBill);

    /**
     * 订单超时自动取消 发送消息
     * @param rentalBill
     */
    void sendOrderOverTimeMessage(RentalOrder rentalBill);

    /**
     * 发消息给付费成功的用户
     * @param order
     */
    void sendRentalSuccessSms(RentalOrder order);

    /**
     * 延时成功发消息
     * @param rentalBill
     */
    void renewRentalOrderSendMessage(RentalOrder rentalBill);

    void endReminderSendMessage(RentalOrder rentalBill);

    void overTimeSendMessage(RentalOrder rentalBill);

    void completeOrderSendMessage(RentalOrder rentalBill);

    void autoCancelOrderSendMessage(RentalOrder rentalBill);

    void autoUpdateOrderSpaceSendMessage(RentalOrder rentalBill);

    default void cancelOrderWithoutPaySendMessage(RentalOrder rentalBill){}

    default void refundOrderSuccessSendMessage(RentalOrder rentalBill){}

    default void cancelOrderWithoutRefund(RentalOrder rentalBill){}

    default void cancelOrderNeedRefund(RentalOrder rentalBill){}
}
