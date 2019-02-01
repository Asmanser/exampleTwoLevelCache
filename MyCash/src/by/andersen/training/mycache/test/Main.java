package by.andersen.training.mycache.test;

import by.andersen.training.mycache.implementation.TwoLevelCacheClass;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Book book1 = new Book("Герберт Шилдт","Java 8. Полное руководство 9-е издание");
        Book book2 = new Book("Александр Полярный","Мятная сказка");
        Book book3 = new Book("Эльчин Сафарли","Когда я вернусь, будь дома");
        Book book4 = new Book("Том Хэнкс","Уникальный экземпляр. Истории о том о сём");
        TwoLevelCacheClass<String,Book> twoLevelCacheClass = new TwoLevelCacheClass<String, Book>(2,1);
        twoLevelCacheClass.cache(book1.getName(),book1);
        twoLevelCacheClass.cache(book2.getName(),book2);
        Book book = twoLevelCacheClass.getObject(book2.getName());
        book = twoLevelCacheClass.getObject(book2.getName());
        twoLevelCacheClass.cache(book3.getName(),book3);
        twoLevelCacheClass.cache(book4.getName(),book4);
        book = twoLevelCacheClass.getObject(book1.getName());
        System.out.println(book);
        twoLevelCacheClass.clearCache();
    }

}
