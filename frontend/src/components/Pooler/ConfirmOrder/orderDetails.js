import React, { Component } from 'react';

class CartProduct extends Component {

    render() {
        let total = this.props.productObj.price * this.props.productObj.quantity
        
        return (
            <div className="row border p-2 text-center">
                <div className="col-md-1">{this.props.slNo}</div>
                <div className="col-md-2"><img src="https://www.okea.org/wp-content/uploads/2019/10/placeholder.png" alt="..." class="img-thumbnail" /></div>
                <div className="col-md-2">{this.props.productObj.name}</div>
                <div className="col-md-2">{this.props.productObj.price.toFixed(2)}</div>
                <div className="col-md-3">{this.props.productObj.quantity}</div>
                <div className="col-md-2">{total.toFixed(2)}</div>
            </div>
        )
    }
}

class ViewCart extends Component {

    constructor() {
        super()
        this.state = {
            confirmOrder: false
        }
    }

    render() {

        let productsInCart = [],
            productObj = {
                name: "Product 1",
                price: 12,
                quantity: 3
            },
            cartSubTotal = 0

        for (var index = 0; index < 10; index++) {

            cartSubTotal = cartSubTotal + (productObj.price * productObj.quantity);
            productsInCart.push(<CartProduct slNo={index + 1} productObj={productObj} editable={this.props.editable} />);
        }
        let tax = cartSubTotal * 0.0925,
            convenienceFee = cartSubTotal * 0.005,
            cartTotal = cartSubTotal + tax + convenienceFee

        return (
            <div className="p-5">
                <p className="display-4 text-center">{this.props.heading}</p>
                <div className="row font-weight-bold bg-secondary p-2 text-white text-center">
                    <div className="col-md-1">Sl. No.</div>
                    <div className="col-md-2">Image</div>
                    <div className="col-md-2">Name</div>
                    <div className="col-md-2">Price</div>
                    <div className="col-md-3">Quantity</div>
                    <div className="col-md-2">Total</div>
                </div>
                {productsInCart}
                <div className="row font-weight-bold bg-secondary p-2 text-white text-center">
                    <div className="col-md-3 offset-md-6">Sub Total</div>
                    <div className="col-md-2">${cartSubTotal.toFixed(2)}</div>
                </div>
                <div className="row font-weight-bold bg-secondary p-2 text-white text-center">
                    <div className="col-md-3 offset-md-6">Tax (9.75%)</div>
                    <div className="col-md-2">${tax.toFixed(2)}</div>
                </div>
                <div className="row font-weight-bold bg-secondary p-2 text-white text-center">
                    <div className="col-md-3 offset-md-6">Convenience fee (0.5%)</div>
                    <div className="col-md-2">${convenienceFee.toFixed(2)}</div>
                </div>
                <div className="row font-weight-bold bg-secondary p-2 text-white text-center">
                    <div className="col-md-3 offset-md-6">Total</div>
                    <div className="col-md-2">${cartTotal.toFixed(2)}</div>
                </div>
            </div>
        )
    }
}
//export ViewCart Component
export default ViewCart;