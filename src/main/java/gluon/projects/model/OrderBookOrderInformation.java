package gluon.projects.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderBookOrderInformation {

    private float price;

    private float qantity;

    public OrderBookOrderInformation() {}

    public OrderBookOrderInformation(float price, float quantity) {
        this.price = price;
        this.qantity = quantity;
    }

    public float total() {
        return price * qantity;
    }

    @Override
    public String toString() {
        return "OrderBookOrderInformation{" +
                "price = " + price +
                ", qantity = " + qantity +
                '}';
    }

}
