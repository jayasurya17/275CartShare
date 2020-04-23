import React, { Component } from 'react';

class CartProduct extends Component {

    constructor() {
        super()
        this.state = {
            quantity: 0
        }
    }

    componentDidMount() {
        this.setState({
            quantity: this.props.productObj.quantity
        })
    }

    decreaseQuantity = () => {
        if (this.state.quantity > 1) {
            this.setState({
                quantity: this.state.quantity - 1
            })
        }
    }

    increaseQuantity = () => {
        this.setState({
            quantity: this.state.quantity + 1
        })
    }

    quantityChangeHandler = (e) => {
        if (e.target.value === "") {
            e.target.value = 0
        }
        if (isNaN(e.target.value) === false) {
            this.setState({
                quantity: parseInt(e.target.value, 10)
            })
        }
    }

    render() {
        let total = this.props.productObj.price * this.state.quantity
        
        return (
            <div className="row border p-2 text-center">
                <div className="col-md-1">{this.props.slNo}</div>
                <div className="col-md-2"><img src="https://www.okea.org/wp-content/uploads/2019/10/placeholder.png" alt="..." class="img-thumbnail" /></div>
                <div className="col-md-2">{this.props.productObj.name}</div>
                <div className="col-md-1">{this.props.productObj.price.toFixed(2)}</div>
                <div className="col-md-3"> 
                    <div className="row">
                        <div className="col-md-2 p-0"><button className="btn btn-danger w-100" onClick={this.decreaseQuantity}>-</button></div>
                        <div className="col-md-8"><input type="text" value={this.state.quantity} className="form-control" onChange={this.quantityChangeHandler} /></div>
                        <div className="col-md-2 p-0"><button className="btn btn-success w-100" onClick={this.increaseQuantity}>+</button></div>
                    </div>
                </div>
                <div className="col-md-2">{total.toFixed(2)}</div>
                <div className="col-md-1"><button className="btn btn-danger">Remove</button></div>
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
            <div className="pl-5 pr-5">
                <p className="display-4 text-center">{this.props.heading}</p>
                <div className="row font-weight-bold bg-secondary p-2 text-white text-center">
                    <div className="col-md-1">Sl. No.</div>
                    <div className="col-md-2">Image</div>
                    <div className="col-md-2">Name</div>
                    <div className="col-md-1">Price</div>
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
                <div className="mt-5 mb-5 text-center">
                    <button className="btn btn-success w-50" onClick={this.props.confirmOrder}>Confirm order</button>
                </div>
            </div>
        )
    }
}
//export ViewCart Component
export default ViewCart;