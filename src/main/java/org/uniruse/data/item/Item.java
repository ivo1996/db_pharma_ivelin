package org.uniruse.data.item;

import org.uniruse.data.AtomicIndex;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author ivelin.dimitrov
 */
public class Item extends AtomicIndex {
    private String sku;// unique
    private String description;
    private double price;
    private double salePrice;
    private double deleted;

    public Item(String sku, String description, double price, double salePrice, double deleted) {
        this.sku = sku;
        this.description = description;
        this.price = price;
        this.salePrice = salePrice;
        this.deleted = deleted;
    }

    public Item() {
    }

    @Override
    public String toString() {
        return "Item [sku=" + sku + ", description=" + description + ", price=" + price + ", salePrice=" + salePrice
                + ", deleted=" + deleted + ", index=" + index + "]";
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public double getDeleted() {
        return deleted;
    }

    public void setDeleted(double deleted) {
        this.deleted = deleted;
    }

    /**
     * Sets the current object state as arguments for statement
     *
     * @param statement
     *                  will add and `sku` to query
     * @throws SQLException
     */
    protected void decorateStatement(PreparedStatement statement) throws SQLException {
        statement.setString(index.getAndIncrement(), sku);
        statement.setString(index.getAndIncrement(), description);
        statement.setDouble(index.getAndIncrement(), price);
        statement.setDouble(index.getAndIncrement(), salePrice);
        statement.setDouble(index.getAndIncrement(), deleted);
    }
}
