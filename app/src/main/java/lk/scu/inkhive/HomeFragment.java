//SA24610703
package lk.scu.inkhive;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class HomeFragment extends Fragment {

    private TextView tvWelcome, tvQuote;
    private ImageView imgBanner;
    private RecyclerView rvPopular, rvRecommend;
    private List<Book> popularBooks, recommendedBooks;
    private BookAdapter popularAdapter, recommendAdapter;

    private String[] quotes = {
            "A room without books is like a body without a soul.",
            "Reading is dreaming with open eyes.",
            "Books are uniquely portable magic.",
            "Today a reader, tomorrow a leader."
    };

    public HomeFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Views
        tvWelcome = view.findViewById(R.id.tvWelcome);
        tvQuote = view.findViewById(R.id.tvQuote);
        imgBanner = view.findViewById(R.id.imgBanner);
        rvPopular = view.findViewById(R.id.rvPopular);
        rvRecommend = view.findViewById(R.id.rvRecommend);

        // banner image
        imgBanner.setImageResource(R.drawable.download);

        // Random quote
        Random random = new Random();
        tvQuote.setText("\"" + quotes[random.nextInt(quotes.length)] + "\"");

        // RecyclerViews setup
        popularBooks = new ArrayList<>();
        recommendedBooks = new ArrayList<>();

        popularAdapter = new BookAdapter(getContext(), popularBooks);
        recommendAdapter = new BookAdapter(getContext(), recommendedBooks);

        rvPopular.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        rvRecommend.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));

        rvPopular.setAdapter(popularAdapter);
        rvRecommend.setAdapter(recommendAdapter);

        // Fetch books
        fetchBooks(popularBooks, popularAdapter, "fantasy");
        fetchBooks(recommendedBooks, recommendAdapter, "science");
        return view;
    }

    private void fetchBooks(List<Book> list, BookAdapter adapter, String query) {
        //API
        String url = "https://www.googleapis.com/books/v1/volumes?q=" + query;

        //Make request using Volley
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray items = response.optJSONArray("items");
                        if (items == null || items.length() == 0) return;

                        // Pick 5 random books
                        //AI (idea)
                        Set<Integer> indices = new HashSet<>();
                        Random random = new Random();
                        while (indices.size() < 5 && indices.size() < items.length()) {
                            indices.add(random.nextInt(items.length()));
                        }//end of AI (idea)

                        for (int i : indices) {
                            JSONObject item = items.getJSONObject(i);
                            JSONObject volumeInfo = item.getJSONObject("volumeInfo");

                            // Title
                            String title = volumeInfo.optString("title", "No Title");

                            // Authors
                            String authors = "Unknown Author";
                            JSONArray authorsArr = volumeInfo.optJSONArray("authors");
                            if (authorsArr != null && authorsArr.length() > 0) {
                                List<String> authorList = new ArrayList<>();
                                for (int j = 0; j < authorsArr.length(); j++) {
                                    authorList.add(authorsArr.optString(j));
                                }
                                authors = TextUtils.join(", ", authorList);
                            }

                            // Page count
                            int pageCount = volumeInfo.optInt("pageCount", 0);

                            // Description
                            String description = volumeInfo.optString("description", "No description available");

                            // Preview link
                            String previewLink = volumeInfo.optString("previewLink", "");

                            // Thumbnail
                            String thumbnail = "";
                            JSONObject imageLinks = volumeInfo.optJSONObject("imageLinks");

                            //AI
                            if (imageLinks != null) {
                                thumbnail = imageLinks.optString("thumbnail", imageLinks.optString("smallThumbnail", ""));
                                thumbnail = thumbnail.replace("http://", "https://");
                            }//End

                            // Add to list
                            list.add(new Book(title, thumbnail, authors, pageCount, description, previewLink));
                        }

                        adapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getContext(), "API Error: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(getContext()).add(request);
    }

}
