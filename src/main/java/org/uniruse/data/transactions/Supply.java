package org.uniruse.data.transactions;


import org.uniruse.data.contragents.Supplier;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * @author ivelin.dimitrov
 */
public class Supply extends Parent<SupplyLine> {
    private long id;
    private Timestamp suppliedAt;
    private double totalPrice;
    private Supplier supplier;

    public Supply() {
    }

    @Override
    public String toString() {
        return "Supply [id=" + id + ", suppliedAt=" + suppliedAt + ", totalPrice=" + totalPrice + ", supplier="
                + supplier + ", lines=" + super.toString() +
                "]";
    }

    public long getId() {
        return id;
    }

    public Supply(long id, Timestamp suppliedAt, double totalPrice, Supplier supplier) {
        super();
        this.id = id;
        this.suppliedAt = suppliedAt;
        this.totalPrice = totalPrice;
        this.supplier = supplier;
    }

    public void setSupplierId(int id) {
        supplier.setId(id);
    }

    public long getSupplierId() {
        return supplier.getId();
    }

    public void setId(long id) {
        this.id = id;
    }

    public Timestamp getSuppliedAt() {
        return suppliedAt;
    }

    public void setSuppliedAt(Timestamp suppliedAt) {
        this.suppliedAt = suppliedAt;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public void decorateStatement(PreparedStatement statement, boolean create) throws SQLException {
        statement.setLong(index.getAndIncrement(), supplier.getId());
        if (create) {
            statement.setTimestamp(index.getAndIncrement(), Timestamp.from(Calendar.getInstance().toInstant()));
        } else {
            statement.setTimestamp(index.getAndIncrement(), suppliedAt);
        }
        statement.setDouble(index.getAndIncrement(), totalPrice);
        if (!create) {
            statement.setLong(index.getAndIncrement(), id);
        }
    }
}
