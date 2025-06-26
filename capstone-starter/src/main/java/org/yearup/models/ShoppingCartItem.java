package org.yearup.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;

public class ShoppingCartItem
{
    private Product product = null;
    private int quantity = 1;
    private BigDecimal discountPercent = BigDecimal.ZERO;
    private BigDecimal lineTotal;


    // Constructor to initialize with product and quantity
    public ShoppingCartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public ShoppingCartItem() {

    }

    public Product getProduct()
    {
        return product;
    }

    public void setProduct(Product product)
    {
        this.product = product;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public BigDecimal getDiscountPercent()
    {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent)
    {
        this.discountPercent = discountPercent;
    }

    @JsonIgnore
    // Method to get product ID. Ignored by JSON serialization
    public int getProductId()
    {
        return this.product.getProductId();
    }

    //  Method returns the lineTotal if set, else calculates it
    public BigDecimal getLineTotal()
    {
        return lineTotal != null ? lineTotal : calculateLineTotal();
    }

    // Method to set line total
    public void setLineTotal(double lineTotal){
        this.lineTotal = BigDecimal.valueOf(lineTotal);
    }

    @JsonIgnore
    // Method to calculate the total
    private BigDecimal calculateLineTotal() {
        BigDecimal basePrice = product.getPrice();
        BigDecimal quantity = new BigDecimal(this.quantity);

        BigDecimal subTotal = basePrice.multiply(quantity);
        BigDecimal discountAmount = subTotal.multiply(discountPercent);

        return subTotal.subtract(discountAmount);
    }


}
