package by.andersen.training.mycache.implementation;

import by.andersen.training.mycache.comparators.CacheComparator;
import by.andersen.training.mycache.interfaces.ICache;
import by.andersen.training.mycache.interfaces.IFrecquencyCallObject;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

public class MemoryCacheClass<KeyType, ValueType extends Serializable> implements ICache<KeyType,ValueType>, IFrecquencyCallObject<KeyType> {

    private HashMap<KeyType,String> hashMap;
    private TreeMap<KeyType,Integer> frequencyMap;

    public MemoryCacheClass() {
        hashMap = new HashMap<>();
        frequencyMap = new TreeMap<>();
        File tempFolder = new File("temp\\");
        if(!tempFolder.exists()){
            tempFolder.mkdirs();
        }
    }

    @Override
    public void cache(KeyType keyType, ValueType valueType) throws IOException, ClassNotFoundException {
        String pathToObject;
        pathToObject = "temp\\" + UUID.randomUUID().toString() + ".temp";

        frequencyMap.put(keyType,1);
        hashMap.put(keyType,pathToObject);
        FileOutputStream fileStream = new FileOutputStream(pathToObject);
        ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);

        objectStream.writeObject(valueType);
        objectStream.flush();
        objectStream.close();
        fileStream.flush();
        fileStream.close();
    }

    @Override
    public ValueType getObject(KeyType keyType) {
        if(hashMap.containsKey(keyType)) {
            String pathToObject = hashMap.get(keyType);
            try
            {
                FileInputStream fileStream = new FileInputStream(pathToObject);
                ObjectInputStream objectStream = new ObjectInputStream(fileStream);


                ValueType deserializedObject =  (ValueType)objectStream.readObject();

                int frecquency = frequencyMap.remove(keyType);
                frequencyMap.put(keyType,++frecquency);


                fileStream.close();
                objectStream.close();

                return deserializedObject;
            }
            catch (IOException ex)
            {
                return null;
            }
            catch (ClassNotFoundException ex)
            {
                return null;
            }
        }
        return null;
    }

    @Override
    public void deleteObject(KeyType keyType) {
        if(hashMap.containsKey(keyType)) {
            String pathToObject = hashMap.get(keyType);

            frequencyMap.remove(keyType);
            File deletingFile = new File(hashMap.remove(keyType));
            deletingFile.delete();
        }
    }

    @Override
    public void clearCache() {
        for(KeyType key : hashMap.keySet()){
            File deletingFile = new File(hashMap.get(key));
            deletingFile.delete();
        }

        hashMap.clear();
        frequencyMap.clear();
    }

    @Override
    public ValueType removeObject(KeyType keyType) {
        if(hashMap.containsKey(keyType))
        {
            ValueType result = this.getObject(keyType);
            this.deleteObject(keyType);
            return result;
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
        CacheComparator comparator = new CacheComparator(frequencyMap);
        TreeMap<KeyType,Integer> sorted = new TreeMap(comparator);
        sorted.putAll(frequencyMap);
        return sorted.keySet();
    }

    @Override
    public int getFrecquencyOfCallingObject(KeyType key) {
        if(hashMap.containsKey(key)){
            return frequencyMap.get(key);
        }
        return  0;
    }
}
