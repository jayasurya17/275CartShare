package com.cartshare.utils;

import java.util.Set;

import com.cartshare.models.OrderItems;

public class OrderDetails {

    public String GenerateProductTableWithPrice(Set<OrderItems> products) {

        String HTMLContent = "";
        
        HTMLContent += "<table>";
        HTMLContent += "<tr>";
        HTMLContent += "<th>Sl No</th>";
        HTMLContent += "<th>Image</th>";
        HTMLContent += "<th style=\"text-align: center\">Name</th>";
        HTMLContent += "<th style=\"text-align: center\">Brand</th>";
        HTMLContent += "<th style=\"text-align: center\">Quantity</th>";
        HTMLContent += "<th style=\"text-align: center\">Cost</th>";
        HTMLContent += "<th style=\"text-align: center\">Price</th>";
        HTMLContent += "</tr>";
        Integer count = 1;
        Float subTotal = (float) 0;
        for (OrderItems product: products) {
            Float price = (float)(product.getProductPrice() * product.getQuantity());
            HTMLContent += "<tr>";
            HTMLContent += "<td>" + count + "</td>";
            HTMLContent += "<td><img src=\"" + product.getProductImage() + "\" style=\"height: 120px;\"></td>";
            HTMLContent += "<td style=\"text-align: center\">" + product.getProductName() + "</td>";
            HTMLContent += "<td style=\"text-align: center\">" + product.getProductBrand() + "</td>";
            HTMLContent += "<td style=\"text-align: center\">" + product.getQuantity() + "</td>";
            HTMLContent += "<td style=\"text-align: center\">" + product.getProductPrice() + " / " +  product.getProductUnit() + "</td>";  
            HTMLContent += "<td style=\"text-align: center\">" + price + "</td>";  
            HTMLContent += "</tr>";
            count++;  
            subTotal += price;
        }
        
        Float tax = (float) (subTotal * 0.0925);
        Float convenienceFee = (float) (subTotal * 0.005);
        Float total = subTotal + tax + convenienceFee;
        
        HTMLContent += "<tr>";
        HTMLContent += "<th></th>";
        HTMLContent += "<th></th>";
        HTMLContent += "<th></th>";
        HTMLContent += "<th></th>";
        HTMLContent += "<th></th>";
        HTMLContent += "<th>Sub Total</th>";
        HTMLContent += "<th>" + subTotal + "</th>";
        HTMLContent += "</tr>";
        HTMLContent += "<tr>";
        HTMLContent += "<th></th>";
        HTMLContent += "<th></th>";
        HTMLContent += "<th></th>";
        HTMLContent += "<th></th>";
        HTMLContent += "<th></th>";
        HTMLContent += "<th>Tax (9.25%)</th>";
        HTMLContent += "<th>" + tax + "</th>";
        HTMLContent += "</tr>";
        HTMLContent += "<tr>";
        HTMLContent += "<th></th>";
        HTMLContent += "<th></th>";
        HTMLContent += "<th></th>";
        HTMLContent += "<th></th>";
        HTMLContent += "<th></th>";
        HTMLContent += "<th>Convenience fee (0.5%)</th>";
        HTMLContent += "<th>" + convenienceFee + "</th>";
        HTMLContent += "</tr>";
        HTMLContent += "<tr>";
        HTMLContent += "<th></th>";
        HTMLContent += "<th></th>";
        HTMLContent += "<th></th>";
        HTMLContent += "<th></th>";
        HTMLContent += "<th></th>";
        HTMLContent += "<th>Total</th>";
        HTMLContent += "<th>" + total + "</th>";
        HTMLContent += "</tr>";
        HTMLContent += "</table>";
        return HTMLContent;
    }


    public String GenerateProductTableWithoutPrice(Set<OrderItems> products) {

        String HTMLContent = "";
        
        HTMLContent += "<table>";
        HTMLContent += "<tr>";
        HTMLContent += "<th>Sl No</th>";
        HTMLContent += "<th>Image</th>";
        HTMLContent += "<th style=\"text-align: center\">Name</th>";
        HTMLContent += "<th style=\"text-align: center\">Brand</th>";
        HTMLContent += "<th style=\"text-align: center\">Quantity</th>";
        HTMLContent += "</tr>";
        Integer count = 1;
        for (OrderItems product: products) {
            HTMLContent += "<tr>";
            HTMLContent += "<td>" + count + "</td>";
            HTMLContent += "<td><img src=\"" + product.getProductImage() + "\" style=\"height: 120px;\"></td>";
            HTMLContent += "<td style=\"text-align: center\">" + product.getProductName() + "</td>";
            HTMLContent += "<td style=\"text-align: center\">" + product.getProductBrand() + "</td>";
            HTMLContent += "<td style=\"text-align: center\">" + product.getQuantity() + "</td>";
            HTMLContent += "</tr>";
            HTMLContent += "</table>";
            count++;  
        }

        return HTMLContent;
    }

}