package org.uniruse.data.transactions;

import org.uniruse.data.AtomicIndex;
import org.uniruse.data.contragents.Shipper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * @author ivelin.dimitrov
 */
public class Shippings extends AtomicIndex {
    private long id;
    private Timestamp createdAt;
    private String district;
    private Shipper shipper;
    private Timestamp shippDate;
    private Timestamp deliveryDate;

    public Shippings(long id, String district, Shipper shipper, Timestamp shippDate, Timestamp deliveryDate) {
        this.id = id;
        this.district = district;
        this.shipper = shipper;
        this.shippDate = shippDate;
        this.deliveryDate = deliveryDate;
    }

    @Override
    public String toString() {
        return "Shippings{" + "createdAt=" + createdAt + ", district='" + district + '\'' + ", shippDate=" + shippDate
                + ", deliveryDate=" + deliveryDate + '}';
    }

    public long getShipperId() {
        return shipper.getId();
    }

    public void setShipperId(int id) {
        shipper.setId(id);
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Timestamp getShippDate() {
        return shippDate;
    }

    public void setShippDate(Timestamp shippDate) {
        this.shippDate = shippDate;
    }

    public Timestamp getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Timestamp deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Shippings() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Shipper getShipper() {
        return shipper;
    }

    public void setShipper(Shipper shipper) {
        this.shipper = shipper;
    }

    public void decorateStatement(PreparedStatement statement, boolean create) throws SQLException {
        if (create) {
            statement.setTimestamp(index.getAndIncrement(), Timestamp.from(Calendar.getInstance().toInstant()));
        }
        statement.setString(index.getAndIncrement(), district);
        statement.setLong(index.getAndIncrement(), shipper.getId());
        statement.setTimestamp(index.getAndIncrement(), shippDate);
        statement.setTimestamp(index.getAndIncrement(), deliveryDate);
        if (!create) {
            statement.setLong(index.getAndIncrement(), id);
        }
    }
}
