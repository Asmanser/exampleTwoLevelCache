package by.andersen.training.mycache.implementation;

import by.andersen.training.mycache.comparators.RamCacheComparator;
import by.andersen.training.mycache.interfaces.ICache;
import by.andersen.training.mycache.interfaces.IFrecquencyCallObject;
import sun.reflect.generics.tree.Tree;

import java.io.IOException;
import java.util.*;

public class RamCacheClass <KeyType, ValueType> implements ICache<KeyType,ValueType>, IFrecquencyCallObject<KeyType> {

    private HashMap<KeyType,ValueType> hashMap;
    private TreeMap<KeyType,Integer> frequencyMap;

    public RamCacheClass() {
        hashMap = new HashMap<>();
        frequencyMap = new TreeMap<>();
    }

    @Override
    public void cache(KeyType keyType, ValueType valueType) throws IOException, ClassNotFoundException {
        hashMap.put(keyType,valueType);
        frequencyMap.put(keyType,1);
    }

    @Override
    public ValueType getObject(KeyType keyType) {
        if(hashMap.containsKey(keyType)) {
            int count = frequencyMap.get(keyType);
            frequencyMap.put(keyType,++count);
            return hashMap.get(keyType);
        }
        return null;
    }

    @Override
    public void deleteObject(KeyType keyType) {
        if(hashMap.containsKey(keyType)) {
            hashMap.remove(keyType);
            frequencyMap.remove(keyType);
        }
    }

    @Override
    public void clearCache() {
        hashMap.clear();
        frequencyMap.clear();
    }

    @Override
    public ValueType removeObject(KeyType keyType) {
        if(hashMap.containsKey(keyType)) {
            frequencyMap.remove(keyType);
            return hashMap.remove(keyType);
        }
        return null;
    }

    @Override
    public boolean containsKey(KeyType keyType) {
        return hashMap.containsKey(keyType);
    }

    @Override
    public int size() {
        return hashMap.size();
    }

    @Override
    public Set<KeyType> getMostFrequentlyUsedKeys() {
        RamCacheComparator ramCacheComparator = new RamCacheComparator(frequencyMap);
        TreeMap<KeyType,Integer> sorted = new TreeMap<>(ramCacheComparator);
        sorted.putAll(frequencyMap);
        return sorted.keySet();
    }

    @Override
    public int getFrecquencyOfCallingObject(KeyType key) {
        if(frequencyMap.containsKey(key)) {
            return frequencyMap.get(key);
        }
        return 0;
    }
}
