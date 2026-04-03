//SA24610770
package lk.scu.inkhive;
import java.util.ArrayList;
import java.util.List;
public class FavouritesManager {
    private static FavouritesManager instance;
    private final List<Book> favoriteBooks;

    private FavouritesManager() {
        favoriteBooks = new ArrayList<>();
    }

    public static FavouritesManager getInstance() {
        if (instance == null) {
            instance = new FavouritesManager();
        }
        return instance;
    }

    public void addBook(Book book) {
        if (!favoriteBooks.contains(book)) {
            favoriteBooks.add(book);
        }
    }

    public List<Book> getFavoriteBooks() {
        return favoriteBooks;
    }

    public void removeBook(Book book) {
        favoriteBooks.remove(book);
    }

    public List<Book> getBooks() {
        return new ArrayList<>(favoriteBooks); // returns a copy
    }
}
