package org.uniruse.data.item;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author ivelin.dimitrov
 */
public class Supplement extends Item {
    private String usefulFor;

    public Supplement() {
    }

    public Supplement(String sku, String description, double price, double salePrice, double deleted,
                      String usefulFor) {
        super(sku, description, price, salePrice, deleted);
        this.usefulFor = usefulFor;
    }

    public String getUsefulFor() {
        return usefulFor;
    }

    public void setUsefulFor(String usefulFor) {
        this.usefulFor = usefulFor;
    }

    @Override
    public String toString() {
        return "Supplement{" + super.toString() + "usefulFor='" + usefulFor + '\'' + '}';
    }

    public void decorateStatement(PreparedStatement statement, boolean create) throws SQLException {
        super.decorateStatement(statement);
        statement.setString(index.getAndIncrement(), getUsefulFor());

        if (!create) { // on update
            statement.setString(index.getAndIncrement(), getSku());
        }
    }
}
