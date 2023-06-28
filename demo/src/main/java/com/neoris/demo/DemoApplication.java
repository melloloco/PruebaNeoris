package com.neoris.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}

@RestController
class PriceController {
    private final List<Price> prices;

    public PriceController() {
        prices = initializePrices();
    }

    @GetMapping("/price")
    public ResponseEntity<PriceResponse> getPrice(
            @RequestParam("applicationDate") LocalDateTime applicationDate,
            @RequestParam("productId") long productId,
            @RequestParam("brandId") int brandId) {

        Price selectedPrice = null;
        int selectedPriority = -1;

        for (Price price : prices) {
            if (price.getBrandId() == brandId &&
                    price.getProductId() == productId &&
                    price.getStartDate().isBefore(applicationDate) &&
                    price.getEndDate().isAfter(applicationDate)) {
                if (price.getPriority() > selectedPriority) {
                    selectedPrice = price;
                    selectedPriority = price.getPriority();
                }
            }
        }

        if (selectedPrice != null) {
            PriceResponse response = new PriceResponse(
                    selectedPrice.getProductId(),
                    selectedPrice.getBrandId(),
                    selectedPrice.getPriceList(),
                    selectedPrice.getStartDate(),
                    selectedPrice.getEndDate(),
                    selectedPrice.getPrice(),
                    selectedPrice.getCurrency()
            );
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    private List<Price> initializePrices() {
        List<Price> prices = new ArrayList<>();

        prices.add(new Price(1, LocalDateTime.parse("2020-06-14T00:00:00"), LocalDateTime.parse("2020-12-31T23:59:59"),
                1, 35455, 0, new BigDecimal("35.50"), "EUR"));
        prices.add(new Price(1, LocalDateTime.parse("2020-06-14T15:00:00"), LocalDateTime.parse("2020-06-14T18:30:00"),
                2, 35455, 1, new BigDecimal("25.45"), "EUR"));
        prices.add(new Price(1, LocalDateTime.parse("2020-06-15T00:00:00"), LocalDateTime.parse("2020-06-15T11:00:00"),
                3, 35455, 1, new BigDecimal("30.50"), "EUR"));
        prices.add(new Price(1, LocalDateTime.parse("2020-06-15T16:00:00"), LocalDateTime.parse("2020-12-31T23:59:59"),
                4, 35455, 1, new BigDecimal("38.95"), "EUR"));
		

        return prices;
    }
}

class Price {
    private int brandId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int priceList;
    private long productId;
    private int priority;
    private BigDecimal price;
    private String currency;

    public Price(int brandId, LocalDateTime startDate, LocalDateTime endDate, int priceList, long productId,
                 int priority, BigDecimal price, String currency) {
        this.brandId = brandId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.priceList = priceList;
        this.productId = productId;
        this.priority = priority;
        this.price = price;
        this.currency = currency;
    }

    public int getBrandId() {
        return brandId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public int getPriceList() {
        return priceList;
    }

    public long getProductId() {
        return productId;
    }

    public int getPriority() {
        return priority;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }
}

class PriceResponse {
    private long productId;
    private int brandId;
    private int priceList;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal price;
    private String currency;

    public PriceResponse(long productId, int brandId, int priceList, LocalDateTime startDate, LocalDateTime endDate,
                         BigDecimal price, String currency) {
        this.productId = productId;
        this.brandId = brandId;
        this.priceList = priceList;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.currency = currency;
    }

	public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public int getPriceList() {
        return priceList;
    }

    public void setPriceList(int priceList) {
        this.priceList = priceList;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
