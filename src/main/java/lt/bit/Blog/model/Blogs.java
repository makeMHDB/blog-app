package lt.bit.Blog.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "blogs")
@NamedQueries({
    @NamedQuery(name = "Blogs.findAll", query = "SELECT b FROM Blogs b ORDER BY b.date ASC"),
    @NamedQuery(name = "Blogs.findById", query = "SELECT b FROM Blogs b WHERE b.id = :id"),
    @NamedQuery(name = "Blogs.findByDate", query = "SELECT b FROM Blogs b WHERE b.date = :date"),
    @NamedQuery(name = "Blogs.findByTitle", query = "SELECT b FROM Blogs b WHERE b.title = :title"),
    @NamedQuery(name = "Blogs.findByCommentCount", query = "SELECT b FROM Blogs b WHERE b.commentCount = :commentCount"),
    @NamedQuery(name = "Blogs.findByLikeCount", query = "SELECT b FROM Blogs b WHERE b.likeCount = :likeCount")})
public class Blogs implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "date", length = 13)
    @Temporal(TemporalType.DATE)
    private Date date;
    @Basic(optional = false)
    @Column(name = "title")
    private String title;
    @Basic(optional = false)
    @Lob
    @Column(name = "text")
    private String text;
    @Basic(optional = false)
    @Column(name = "comment_count")
    private int commentCount;
    @Basic(optional = false)
    @Column(name = "like_count")
    private int likeCount;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "blogId")
    private List<Comments> commentsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "blogId")
    private List<Likes> likesList;
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @ManyToOne
    private Categories categoryId;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users author;

    public Blogs() {
    }

    public Blogs(Integer id) {
        this.id = id;
    }

    public Blogs(Integer id, Date date, String title, String text, int commentCount, int likeCount) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.text = text;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

//    public int getCommentCount() {
//        return commentCount;
//    }
    public int getCommentCount() {
        return commentsList.size();
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

//    public int getLikeCount() {
//        return likeCount;
//    }
    public int getLikeCount() {
        return likesList.size();
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public List<Comments> getCommentsList() {
        return commentsList;
    }

    public void setCommentsList(List<Comments> commentsList) {
        this.commentsList = commentsList;
    }

    public Categories getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Categories categoryId) {
        this.categoryId = categoryId;
    }

    public Users getAuthor() {
        return author;
    }

    public void setAuthor(Users author) {
        this.author = author;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Blogs)) {
            return false;
        }
        Blogs other = (Blogs) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Blogs{" + "id=" + id + ", date=" + date + ", title=" + title + ", text=" + text + ", commentCount=" + commentCount + ", likeCount=" + likeCount + '}';
    }

    public List<Likes> getLikesList() {
        return likesList;
    }

    public void setLikesList(List<Likes> likesList) {
        this.likesList = likesList;
    }

}
