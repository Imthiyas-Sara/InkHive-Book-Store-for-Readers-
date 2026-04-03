package lk.scu.inkhive;

//Sa24610703
// basic java class for implementing a book related details
public class Book {
        private String title;
        private String thumbnail;
        private String author;
        private int pageCount;
        private String description;
        private String previewLink;


// home page
    public Book(String title, String thumbnail) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.author = "Unknown";
        this.pageCount = 0;
        this.description = "No description available.";
        this.previewLink = "";
    }

//details page
    public Book(String title, String thumbnail, String author, int pageCount, String description, String previewLink) {
            this.title = title;
            this.thumbnail = thumbnail;
            this.author = author;
            this.pageCount = pageCount;
            this.description = description;
            this.previewLink = previewLink;
        }

        public String getTitle() {
            return title;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public String getAuthor() {
            return author;
        }

        public int getPageCount() {
            return pageCount;
        }

        public String getDescription() {
            return description;
        }

        public String getPreviewLink() {
            return previewLink;
        }
}
