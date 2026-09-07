package com.personalplatform.model;

import jakarta.persistence.*;

@Entity
@Table(
        name = "store_data",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_store_category_format",
                        columnNames = {
                                "store_number",
                                "category_normalized",
                                "format"
                        }
                )
        }
)
public class StoreData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "store_number", nullable = false)
    private Integer storeNumber;

    @Column(name = "store_name", nullable = false)
    private String storeName;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "category_normalized", nullable = false)
    private String categoryNormalized;

    @Column(name = "format", nullable = false)
    private Integer format;

    @Column(name = "sales", nullable = false)
    private Long sales;

    public StoreData() {
    }

    public StoreData(
            Integer storeNumber,
            String storeName,
            String category,
            String categoryNormalized,
            Integer format,
            Long sales
    ) {
        this.storeNumber = storeNumber;
        this.storeName = storeName;
        this.category = category;
        this.categoryNormalized = categoryNormalized;
        this.format = format;
        this.sales = sales;
    }

    public Long getId() {
        return id;
    }

    public Integer getStoreNumber() {
        return storeNumber;
    }

    public void setStoreNumber(Integer storeNumber) {
        this.storeNumber = storeNumber;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryNormalized() {
        return categoryNormalized;
    }

    public void setCategoryNormalized(String categoryNormalized) {
        this.categoryNormalized = categoryNormalized;
    }

    public Integer getFormat() {
        return format;
    }

    public void setFormat(Integer format) {
        this.format = format;
    }

    public Long getSales() {
        return sales;
    }

    public void setSales(Long sales) {
        this.sales = sales;
    }
}
