package entity;

public class OrderDetail_PK {
    private String orderId;
    private String itemCode;

    public OrderDetail_PK() {
    }

    public OrderDetail_PK(String orderId, String itemCode) {
        this.orderId = orderId;
        this.itemCode = itemCode;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
}
