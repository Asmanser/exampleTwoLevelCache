package by.andersen.training.mycache.interfaces;

import java.io.IOException;

public interface ICache<KeyType,ValueType> {

    void cache(KeyType keyType, ValueType valueType) throws IOException,ClassNotFoundException;
    ValueType getObject(KeyType keyType) throws IOException, ClassNotFoundException;
    void deleteObject(KeyType keyType);
    void clearCache();
    ValueType removeObject(KeyType keyType);
    boolean containsKey(KeyType keyType);
    int size();

}
