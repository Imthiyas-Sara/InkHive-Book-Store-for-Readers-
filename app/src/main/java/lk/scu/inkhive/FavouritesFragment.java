//SA24610770
package lk.scu.inkhive;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import java.util.List;

public class FavouritesFragment extends Fragment {

    private LinearLayout favoritesContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_favourites, container, false);
        favoritesContainer = root.findViewById(R.id.favoritesContainer);

        populateFavorites();

        return root;
    }

    private void populateFavorites() {
        favoritesContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getContext());

        List<Book> favoriteBooks = FavouritesManager.getInstance().getBooks();

        //AI
        for (Book book : favoriteBooks) {
            View card = inflater.inflate(R.layout.item_favourite_book, favoritesContainer, false);

            ImageView img = card.findViewById(R.id.imgBookThumbnail);
            TextView tvTitle = card.findViewById(R.id.tvBookTitle);
            Button btnView = card.findViewById(R.id.btnView);
            Button btnRemove = card.findViewById(R.id.btnRemove);

            tvTitle.setText(book.getTitle());
            Glide.with(this).load(book.getThumbnail()).into(img);

            //AI
            btnView.setOnClickListener(v -> {
                // Open DetailsFragment dynamically
                Bundle bundle = new Bundle();
                bundle.putString("title", book.getTitle());
                bundle.putString("author", book.getAuthor());
                bundle.putInt("pageCount", book.getPageCount());
                bundle.putString("description", book.getDescription());
                bundle.putString("thumbnail", book.getThumbnail());
                bundle.putString("previewLink", book.getPreviewLink());

                DetailsFragment fragment = new DetailsFragment();
                fragment.setArguments(bundle);

                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_Layout, fragment)
                        .addToBackStack(null)
                        .commit();
            });


            btnRemove.setOnClickListener(v -> {
                FavouritesManager.getInstance().removeBook(book);
                populateFavorites(); // refresh list
                Toast.makeText(getContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
            });

            favoritesContainer.addView(card);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        populateFavorites(); // refresh every time fragment becomes visible
    }

    //https://youtu.be/Ov9-BsLb8_w?si=oCq6ttjQGu0M-nBO
    //https://youtu.be/-BX1egoRSVs?si=HT8WyoSii0gxGXUE
    //https://youtu.be/mrzITuepiFM?si=yzTemQho_Td7nu8o
}
