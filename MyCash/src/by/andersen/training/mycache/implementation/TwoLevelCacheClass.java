package by.andersen.training.mycache.implementation;

import by.andersen.training.mycache.interfaces.ILeveledCache;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class TwoLevelCacheClass<KeyType,ValueType extends Serializable> implements ILeveledCache<KeyType,ValueType> {

    private RamCacheClass<KeyType,ValueType> ramCache;
    private MemoryCacheClass<KeyType,ValueType> memoryCache;
    private int maxRamCacheCapacity;
    private int maxMemoryCacheCapacity;
    private int numberOfRequests;
    private int numberOfRequestsForRecahce;

    public TwoLevelCacheClass(int maxRamCacheCapacity, int numberOfRequestsForRecache,int maxMemoryCacheCapacity)
    {
        this.maxMemoryCacheCapacity = maxMemoryCacheCapacity;
        this.maxRamCacheCapacity = maxRamCacheCapacity;
        this.numberOfRequestsForRecahce = numberOfRequestsForRecache;
        ramCache = new RamCacheClass<KeyType, ValueType>();
        memoryCache = new MemoryCacheClass<KeyType, ValueType>();
        numberOfRequests = 0;
    }


    @Override
    public void recache() throws IOException, ClassNotFoundException {
        TreeSet<KeyType> ramKeySet = new TreeSet<KeyType>(ramCache.getMostFrequentlyUsedKeys());
        int boundFrecquency = 0;


        // вычисление среднего арифметического для отбрасывания редко вызываемых объектов
        for(KeyType key: ramKeySet)
        {
            boundFrecquency += ramCache.getFrecquencyOfCallingObject(key);
        }
        boundFrecquency /= ramKeySet.size();

        freeSpaceInMemoryCache(ramKeySet,boundFrecquency);

        for(KeyType key: ramKeySet)
        {
            if(ramCache.getFrecquencyOfCallingObject(key) <= boundFrecquency){
                memoryCache.cache(key,ramCache.removeObject(key));
            }
        }

        TreeSet<KeyType> memoryKeySet = new TreeSet<KeyType>(memoryCache.getMostFrequentlyUsedKeys());
        for(KeyType key : memoryKeySet)
        {
            try{
                if(memoryCache.getFrecquencyOfCallingObject(key) > boundFrecquency)
                {
                    ramCache.cache(key,memoryCache.removeObject(key));
                }
            }
            catch (IOException ex)
            {
                memoryCache.deleteObject(key);
                continue;
            }
            catch (ClassNotFoundException ex)
            {
                ex.printStackTrace();
                continue;
            }
        }
    }

    private void freeSpaceInMemoryCache(TreeSet<KeyType> ramKeySet,int boundFrecquency) throws IOException, ClassNotFoundException {
        int countElement = 0;
        for(KeyType key: ramKeySet)
        {
            if(ramCache.getFrecquencyOfCallingObject(key) <= boundFrecquency){
                countElement++;
            }
        }
        if(countElement > (maxMemoryCacheCapacity-memoryCache.size())) {
            if(memoryCache.size() >= maxMemoryCacheCapacity) {
                Iterator<KeyType> iterator = memoryCache.getMostFrequentlyUsedKeys().iterator();
                KeyType keyType = null;
                int i = 1;
                while(iterator.hasNext()) {
                    keyType = iterator.next();
                    if(keyType != null) {
                        memoryCache.removeObject(keyType);
                    }
                    if(i != (countElement - maxMemoryCacheCapacity-memoryCache.size())) {
                        i++;
                    } else {
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void cache(KeyType keyType, ValueType valueType) throws IOException, ClassNotFoundException {
        if(!(ramCache.size() >= maxRamCacheCapacity)) {
            ramCache.cache(keyType, valueType);
        } else {
            recache();
            ramCache.cache(keyType, valueType);
        }
    }

    @Override
    public ValueType getObject(KeyType keyType) throws IOException, ClassNotFoundException {
        if(ramCache.containsKey(keyType)){
            numberOfRequests++;
            if(numberOfRequests > numberOfRequestsForRecahce){
                this.recache();
                numberOfRequests = 0;
            }
            return ramCache.getObject(keyType);
        }
        if(memoryCache.containsKey(keyType)){
            numberOfRequests++;
            if(numberOfRequests > numberOfRequestsForRecahce){
                this.recache();
                numberOfRequests = 0;
            }
            return  memoryCache.getObject(keyType);
        }
        return null;
    }

    @Override
    public void deleteObject(KeyType keyType) {
        if(ramCache.containsKey(keyType)){
            ramCache.deleteObject(keyType);
        }
        if(memoryCache.containsKey(keyType)){
            memoryCache.deleteObject(keyType);
        }
    }

    @Override
    public void clearCache() {
        memoryCache.clearCache();
        ramCache.clearCache();
    }

    @Override
    public ValueType removeObject(KeyType keyType) {
        if(ramCache.containsKey(keyType)){
            return ramCache.removeObject(keyType);
        }
        if(memoryCache.containsKey(keyType)){
            return  memoryCache.removeObject(keyType);
        }
        return null;
    }

    @Override
    public boolean containsKey(KeyType keyType) {
        if(ramCache.containsKey(keyType)){
            return true;
        }
        if(memoryCache.containsKey(keyType)){
            return  true;
        }
        return false;
    }

    @Override
    public int size() {
        return ramCache.size() + memoryCache.size();
    }

    @Override
    public Set<KeyType> getMostFrequentlyUsedKeys() {
        TreeSet<KeyType> set = new TreeSet<KeyType>(ramCache.getMostFrequentlyUsedKeys());
        set.addAll(memoryCache.getMostFrequentlyUsedKeys());
        return set;
    }

    @Override
    public int getFrecquencyOfCallingObject(KeyType key) {
        if(ramCache.containsKey(key))
        {
            return ramCache.getFrecquencyOfCallingObject(key);
        }
        if(memoryCache.containsKey(key))
        {
            return memoryCache.getFrecquencyOfCallingObject(key);
        }
        return 0;
    }
}
