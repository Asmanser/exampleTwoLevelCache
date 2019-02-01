package by.andersen.training.mycache.interfaces;

import java.util.Set;

public interface IFrecquencyCallObject<KeyType> {

    Set<KeyType> getMostFrequentlyUsedKeys();
    int getFrecquencyOfCallingObject(KeyType key);

}
