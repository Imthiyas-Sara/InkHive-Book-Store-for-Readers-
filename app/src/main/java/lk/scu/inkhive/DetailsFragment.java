//SA24610519
package lk.scu.inkhive;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class DetailsFragment extends Fragment {

    private EditText etSearchBook;
    private ImageButton btnSearch;

    private LinearLayout bookLayout;
    private ImageView imgBookCover;
    private TextView tvBookTitle, tvAuthor, tvPageCount, tvDescription;
    private Button btnPreview;
    private ImageButton btnLike;

    // Current book displayed
    private Book currentBook;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_details, container, false);

        etSearchBook = root.findViewById(R.id.etSearchBook);
        btnSearch = root.findViewById(R.id.btnSearch);

        // Inflate book details layout
        bookLayout = (LinearLayout) inflater.inflate(R.layout.item_book_details, container, false);
        imgBookCover = bookLayout.findViewById(R.id.imgBookCover);
        tvBookTitle = bookLayout.findViewById(R.id.tvBookTitle);
        tvAuthor = bookLayout.findViewById(R.id.tvAuthor);
        tvPageCount = bookLayout.findViewById(R.id.tvPageCount);
        tvDescription = bookLayout.findViewById(R.id.tvDescription);
        btnPreview = bookLayout.findViewById(R.id.btnPreview);
        btnLike = bookLayout.findViewById(R.id.btnLike);

        bookLayout.setVisibility(View.GONE);

        // Add book layout below search bar
        View searchBar = root.findViewById(R.id.searchBarLayout);
        //AI
        if (searchBar != null && searchBar.getParent() instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) searchBar.getParent();
            int indexOfSearchBar = parent.indexOfChild(searchBar);
            parent.addView(bookLayout, indexOfSearchBar + 1);
        } else {
            ((ViewGroup) root).addView(bookLayout);
        }

        // Search button
        btnSearch.setOnClickListener(v -> startSearch());

        // Keyboard search : trigger search when pressed on keyboard
        etSearchBook.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                startSearch();
                return true;
            }
            return false;
        });
        // end of AI

        // Preview button - leads to browser when pressed
        btnPreview.setOnClickListener(v -> {
            if (currentBook != null && !currentBook.getPreviewLink().isEmpty()) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(currentBook.getPreviewLink()));
                startActivity(i); //launch browser
            } else {
                Toast.makeText(getContext(), "No preview available", Toast.LENGTH_SHORT).show();
            }
        });

        // Like button
        //SA24610770
        btnLike.setOnClickListener(v -> {
            if (currentBook != null) {
                FavouritesManager.getInstance().addBook(currentBook);
                Toast.makeText(getContext(), "Added to favorites!", Toast.LENGTH_SHORT).show();
            }
        });

        // Check if we have arguments passed (when clicked from HomeFragment)
        //AI
        Bundle args = getArguments();
        if (args != null) {
            currentBook = new Book(
                    args.getString("title"),
                    args.getString("thumbnail"),
                    args.getString("author"),
                    args.getInt("pageCount"),
                    args.getString("description"),
                    args.getString("previewLink")
            );
            populateBook(currentBook);
        }

        return root;
    }//End of AI

    private void startSearch() {//implemented search function
        String query = etSearchBook.getText() != null ? etSearchBook.getText().toString().trim() : "";
        if (TextUtils.isEmpty(query)) {
            Toast.makeText(getContext(), "Enter a book title", Toast.LENGTH_SHORT).show();
        } else {
            new FetchBookTask().execute(query); //validate the search book
        }
    }

    private void populateBook(Book book) {// update search UI
        tvBookTitle.setText(book.getTitle());
        tvAuthor.setText("By " + book.getAuthor());
        tvPageCount.setText("Pages: " + book.getPageCount());
        tvDescription.setText(book.getDescription());

        if (!book.getThumbnail().isEmpty()) {
            Glide.with(requireContext()).load(book.getThumbnail()).into(imgBookCover);
        } else {
            imgBookCover.setImageResource(R.drawable.download);
        }

        bookLayout.setVisibility(View.VISIBLE);
    }

    private class FetchBookTask extends AsyncTask<String, Void, JSONObject> {

        private Exception backgroundException = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getContext(), "Searching...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            String query = params[0];
            HttpsURLConnection connection = null;
            try {
                String encoded = URLEncoder.encode(query, "UTF-8");
                String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=" + encoded + "&printType=books&maxResults=10";
                URL url = new URL(apiUrl);

                connection = (HttpsURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                //AI
                connection.setConnectTimeout(10_000);
                connection.setReadTimeout(10_000);

                int code = connection.getResponseCode();
                InputStream is = (code >= 200 && code < 300) ? connection.getInputStream() : connection.getErrorStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();

                return new JSONObject(sb.toString());

            } catch (Exception e) {
                e.printStackTrace();
                backgroundException = e;
                return null;
            } finally {
                if (connection != null) connection.disconnect();
            }
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            if (json == null) {
                Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_LONG).show();
                bookLayout.setVisibility(View.GONE);
                return;
            }

            try {
                JSONArray items = json.optJSONArray("items");
                if (items == null || items.length() == 0) {
                    Toast.makeText(getContext(), "No book found", Toast.LENGTH_SHORT).show();
                    bookLayout.setVisibility(View.GONE);
                    return;
                }

                JSONObject volumeInfo = items.getJSONObject(0).getJSONObject("volumeInfo");

                String title = volumeInfo.optString("title", "No Title");
                String authors = "Unknown Author";

                if (volumeInfo.has("authors")) {
                    JSONArray authorsArr = volumeInfo.optJSONArray("authors");
                    if (authorsArr != null && authorsArr.length() > 0) {
                        List<String> authorList = new ArrayList<>();
                        for (int i = 0; i < authorsArr.length(); i++) {
                            authorList.add(authorsArr.optString(i));
                        }
                        authors = TextUtils.join(", ", authorList);
                    }
                }

                int pageCount = volumeInfo.optInt("pageCount", 0);
                String description = volumeInfo.optString("description", "No description available");
                String previewLink = volumeInfo.optString("previewLink", "");

                String imageLink = "";
                if (volumeInfo.has("imageLinks")) {
                    JSONObject imgObj = volumeInfo.optJSONObject("imageLinks");
                    if (imgObj != null) {
                        imageLink = imgObj.optString("thumbnail", imgObj.optString("smallThumbnail", ""));
                        if (imageLink.startsWith("http:")) {
                            imageLink = imageLink.replace("http:", "https:");
                        }
                    }
                }

                // Update currentBook
                currentBook = new Book(title, imageLink, authors, pageCount, description, previewLink);
                populateBook(currentBook);
                //parse-json ->authors,pagecount,etc..
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error parsing response", Toast.LENGTH_SHORT).show();
                bookLayout.setVisibility(View.GONE);
            }
        }
    }
}
