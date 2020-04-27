package com.cartshare.utils;

import java.util.Set;

import com.cartshare.models.OrderItems;

public class OrderDetails {

    // public String GenerateProductTableWithPrice(Set<OrderItems> products) {

    //     String HTMLContent = "";
    //     HTMLContent += "<!DOCTYPE html>";
    //     HTMLContent += "<html lang=\"en\">";
    //     HTMLContent += "<head>";
    //     HTMLContent += "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\">";
    //     HTMLContent += "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css\">";
    //     HTMLContent += "<link rel=\"stylesheet\" href=\"https://fonts.googleapis.com/icon?family=Material+Icons\">";
    //     HTMLContent += "</head>";
    //     HTMLContent += "<body>";
    //     HTMLContent += "<div class=\"row\">";
    //     HTMLContent += "<div class=\"col-md-1\">Sl No</div>";
    //     HTMLContent += "<div class=\"col-md-3\">Image</div>";
    //     HTMLContent += "<div class=\"col-md-2\">Name</div>";
    //     HTMLContent += "<div class=\"col-md-2\">Brand</div>";
    //     HTMLContent += "<div class=\"col-md-2\">Quantity</div>";
    //     HTMLContent += "<div class=\"col-md-2\">Price</div>";
    //     HTMLContent += "</div>";
    //     Integer count = 1;
    //     Float subTotal = (float) 0;
    //     for (OrderItems product: products) {
    //         Float price = (float)(product.getProductPrice() * product.getQuantity());
    //         HTMLContent += "<div class=\"row\">";
    //         HTMLContent += "<div class=\"col-md-1\">" + count + "</div>";
    //         HTMLContent += "<div class=\"col-md-3\"><img src=\"" + product.getProductImage() + "\" class=\"img-thumbnail\"></div>";
    //         HTMLContent += "<div class=\"col-md-2\">" + product.getProductName() + "</div>";
    //         HTMLContent += "<div class=\"col-md-2\">" + product.getProductBrand() + "</div>";
    //         HTMLContent += "<div class=\"col-md-2\">" + product.getQuantity() + "</div>";
    //         HTMLContent += "<div class=\"col-md-2\">" + price + "</div>";  
    //         HTMLContent += "</div>";
    //         count++;  
    //         subTotal += price;
    //     }
        
    //     Float tax = (float) (subTotal * 0.0975);
    //     Float convenienceFee = (float) (subTotal * 0.005);
    //     Float total = subTotal + tax + convenienceFee;
    //     HTMLContent += "<div class=\"row font-weight-bold bg-secondary p-2 text-white text-center\">";
    //     HTMLContent += "<div class=\"col-md-3 offset-md-6\">Sub Total</div>";
    //     HTMLContent += "<div class=\"col-md-2\">" + subTotal + "</div>";
    //     HTMLContent += "</div>";
    //     HTMLContent += "<div class=\"row font-weight-bold bg-secondary p-2 text-white text-center\">";
    //     HTMLContent += "<div class=\"col-md-3 offset-md-6\">Tax (9.75%)</div>";
    //     HTMLContent += "<div class=\"col-md-2\">" + tax + "</div>";
    //     HTMLContent += "</div>";
    //     HTMLContent += "<div class=\"row font-weight-bold bg-secondary p-2 text-white text-center\">";
    //     HTMLContent += "<div class=\"col-md-3 offset-md-6\">Convenience fee (0.5%)</div>";
    //     HTMLContent += "<div class=\"col-md-2\">" + convenienceFee + "</div>";
    //     HTMLContent += "</div>";
    //     HTMLContent += "<div class=\"row font-weight-bold bg-secondary p-2 text-white text-center\">";
    //     HTMLContent += "<div class=\"col-md-3 offset-md-6\">Total</div>";
    //     HTMLContent += "<div class=\"col-md-2\">" + total + "</div>";
    //     HTMLContent += "</div>";
    //     HTMLContent += "</body>";
    //     HTMLContent += "</html>";
    //     return HTMLContent;
    // }

    public String GenerateProductTableWithPrice(Set<OrderItems> products) {

        String HTMLContent = "";
        
        HTMLContent += "<table style=\"background-color: #f8f9fa\">";
        HTMLContent += "<tr style=\"background-color: #6c757d\">";
        HTMLContent += "<th>Sl No</th>";
        HTMLContent += "<th>Image</th>";
        HTMLContent += "<th>Name</th>";
        HTMLContent += "<th>Brand</th>";
        HTMLContent += "<th>Quantity</th>";
        HTMLContent += "<th>Price</th>";
        HTMLContent += "</tr>";
        Integer count = 1;
        Float subTotal = (float) 0;
        for (OrderItems product: products) {
            Float price = (float)(product.getProductPrice() * product.getQuantity());
            HTMLContent += "<tr>";
            HTMLContent += "<td>" + count + "</td>";
            HTMLContent += "<td><img src=\"" + product.getProductImage() + "\" style=\"height: 120px;\"></td>";
            HTMLContent += "<td>" + product.getProductName() + "</td>";
            HTMLContent += "<td>" + product.getProductBrand() + "</td>";
            HTMLContent += "<td style=\"text-align: center\">" + product.getQuantity() + "</td>";
            HTMLContent += "<td>" + price + "</td>";  
            HTMLContent += "</tr>";
            count++;  
            subTotal += price;
        }
        
        Float tax = (float) (subTotal * 0.0975);
        Float convenienceFee = (float) (subTotal * 0.005);
        Float total = subTotal + tax + convenienceFee;
        
        HTMLContent += "<tr>";
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
        HTMLContent += "<th>Tax (9.75%)</th>";
        HTMLContent += "<th>" + tax + "</th>";
        HTMLContent += "</tr>";
        HTMLContent += "<tr>";
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
        HTMLContent += "<th>Total</th>";
        HTMLContent += "<th>" + total + "</th>";
        HTMLContent += "</tr>";
        return HTMLContent;
    }

}