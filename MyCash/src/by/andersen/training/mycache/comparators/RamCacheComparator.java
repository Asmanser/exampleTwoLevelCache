package by.andersen.training.mycache.comparators;

import java.util.Comparator;
import java.util.Map;

public class RamCacheComparator implements Comparator {

    Map base;

    public RamCacheComparator(Map base) {
        this.base = base;
    }

    @Override
    public int compare(Object o1, Object o2) {
        if((Integer)base.get(o1) < (Integer)base.get(o2)) {
            return 1;
        } else {
            if((Integer)base.get(o1) > (Integer)base.get(o2)) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
