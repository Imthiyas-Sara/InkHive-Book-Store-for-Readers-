package lk.scu.inkhive;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

//SA24610703
// lab notes
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private Context context;
    private List<Book> bookList;

    public BookAdapter(Context context, List<Book> bookList){
        this.context = context;
        this.bookList = bookList;
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBook, btnFavourite;
        TextView tvBookTitle;
        Button btnView;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBook = itemView.findViewById(R.id.imgBook);
            tvBookTitle = itemView.findViewById(R.id.tvBookTitle);
            btnFavourite = itemView.findViewById(R.id.btnFavourite);
            btnView = itemView.findViewById(R.id.btnView);
        }
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false); //creates the layout for one book item.
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);

        holder.tvBookTitle.setText(book.getTitle());

        // Load cover image
        if (book.getThumbnail() != null && !book.getThumbnail().isEmpty()) {
            Picasso.get()
                    .load(book.getThumbnail())
                    .placeholder(R.drawable.download)
                    .into(holder.imgBook);
        } else {
            holder.imgBook.setImageResource(R.drawable.download);
        }


        // Favourite click
        //SA24610770
        holder.btnFavourite.setOnClickListener(v -> {
            FavouritesManager.getInstance().addBook(book);
            Toast.makeText(context, "Added to favorites!", Toast.LENGTH_SHORT).show();
        });

        // View click
        holder.btnView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("title", book.getTitle());
            bundle.putString("author", book.getAuthor());
            bundle.putInt("pageCount", book.getPageCount());
            bundle.putString("description", book.getDescription());
            bundle.putString("thumbnail", book.getThumbnail());
            bundle.putString("previewLink", book.getPreviewLink());

            DetailsFragment fragment = new DetailsFragment();
            fragment.setArguments(bundle);

            ((FragmentActivity) context).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_Layout, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
}
