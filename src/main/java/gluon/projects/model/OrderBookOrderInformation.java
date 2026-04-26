package gluon.projects.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderBookOrderInformation {

    private float price;

    private float quantity;

    public OrderBookOrderInformation() {}

    public OrderBookOrderInformation(float price, float quantity) {
        this.price = price;
        this.quantity = quantity;
    }

    public float total() {
        return price * quantity;
    }

    @Override
    public String toString() {
        return "OrderBookOrderInformation{" +
                "price = " + price +
                ", qantity = " + quantity +
                '}';
    }

}
