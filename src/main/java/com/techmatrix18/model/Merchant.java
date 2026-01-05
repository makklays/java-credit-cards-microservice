package com.techmatrix18.model;

import com.techmatrix18.enums.MerchantCategory;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entity representing a merchant/business.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.12.2025
 */
@Table("merchants")
public class Merchant {
    @Id
    @Column("id")
    private Long id;

    @Column("title")
    private String title;

    @Column("description")
    private String description;

    @Column("category")
    private MerchantCategory category;

    @Column("location")
    private String location;        // адрес или город/страна

    @Column("phone")
    private String phone;

    @Column("email")
    private String email;

    @Column("website")
    private String website;

    @Column("rating")
    private int rating;             // рейтинг торговой точки (например, от 1 до 5)

    @Column("latitude")
    private Double latitude;

    @Column("longitude")
    private Double longitude;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    // --- Getters and Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public MerchantCategory getCategory() { return category; }
    public void setCategory(MerchantCategory category) { this.category = category; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Merchant merchant = (Merchant) o;
        return rating == merchant.rating &&
            Objects.equals(id, merchant.id) &&
            Objects.equals(title, merchant.title) &&
            Objects.equals(category, merchant.category) &&
            Objects.equals(location, merchant.location) &&
            Objects.equals(phone, merchant.phone) &&
            Objects.equals(email, merchant.email) &&
            Objects.equals(website, merchant.website) &&
            Objects.equals(latitude, merchant.latitude) &&
            Objects.equals(longitude, merchant.longitude);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (website != null ? website.hashCode() : 0);
        result = 31 * result + rating;
        result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);

        return result;
    }

    @Override
    public String toString() {
        return "Merchant {" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", description='" + description + '\'' +
            ", category='" + category + '\'' +
            ", location='" + location + '\'' +
            ", phone='" + phone + '\'' +
            ", email='" + email + '\'' +
            ", website='" + website + '\'' +
            ", rating=" + rating +
            ", latitude=" + latitude +
            ", longitude=" + longitude +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            '}';
    }
}

