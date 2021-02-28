/**
 *
 */
package org.uniruse.data.transactions;

import org.uniruse.data.AtomicIndex;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ivelin.dimitrov
 */
public class Parent<T> extends AtomicIndex {
    private List<T> childs = new ArrayList<>();

    public List<T> getChilds() {
        return childs;
    }

    public void setChilds(List<T> childs) {
        this.childs = childs;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        childs.forEach(builder::append);
        return "Parent [childs=" + builder.toString() + "]";
    }
}
