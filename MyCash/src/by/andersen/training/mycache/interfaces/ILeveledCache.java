package by.andersen.training.mycache.interfaces;

import java.io.IOException;

public interface ILeveledCache <KeyType,ValueType> extends ICache<KeyType,ValueType>,IFrecquencyCallObject<KeyType>{
    void recache() throws IOException, ClassNotFoundException;
}
