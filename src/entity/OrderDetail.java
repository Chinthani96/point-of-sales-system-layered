package entity;

import java.math.BigDecimal;

public class OrderDetail {
    private OrderDetail_PK orderDetail_pk;
    private int qty;
    private BigDecimal unitPrice;

    public OrderDetail() {
    }

    public OrderDetail(OrderDetail_PK orderDetail_pk, int qty, BigDecimal unitPrice) {
        this.orderDetail_pk = orderDetail_pk;
        this.qty = qty;
        this.unitPrice = unitPrice;
    }

    public OrderDetail(String orderId, String itemCode, int qty, BigDecimal unitPrice) {
        this.orderDetail_pk = new OrderDetail_PK(orderId,itemCode);
        this.qty = qty;
        this.unitPrice = unitPrice;
    }

    public OrderDetail_PK getOrderDetail_pk() {
        return orderDetail_pk;
    }

    public void setOrderDetail_pk(OrderDetail_PK orderDetail_pk) {
        this.orderDetail_pk = orderDetail_pk;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}
