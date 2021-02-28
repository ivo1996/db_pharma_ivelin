package org.uniruse.data.transactions;

import org.uniruse.data.AtomicIndex;
import org.uniruse.data.item.Item;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author ivelin.dimitrov
 */
public class OrderLines extends AtomicIndex {
    private long orderId;
    private Item item;
    private double quantity;
    private double price;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public OrderLines(long orderId, Item item, double quantity) {
        this.orderId = orderId;
        this.item = item;
        this.quantity = quantity;
        this.price = item.getPrice() * quantity;
    }

    public OrderLines() {
    }

    @Override
    public String toString() {
        return "OrderLines{" +
                "orderId=" + orderId +
                ", item=" + item +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }

    public void decorateStatement(PreparedStatement statement) throws SQLException {
        statement.setLong(index.getAndIncrement(), orderId);
        statement.setString(index.getAndIncrement(), item.getSku());
        statement.setDouble(index.getAndIncrement(), quantity);
        statement.setDouble(index.getAndIncrement(), price);
    }
}
