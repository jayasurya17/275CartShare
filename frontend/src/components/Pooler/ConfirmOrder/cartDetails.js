import React, { Component } from 'react';
import axios from 'axios';


class ViewCart extends Component {

    constructor() {
        super()
        this.state = {
            allProducts: [],
            confirmOrder: false,
            isFetched: false
        }
    }

    componentDidMount() {
        this.getAllProductsIncart();
    }

    getAllProductsIncart = () => {
        axios.get(`/orders/productsInCart?userId=${localStorage.getItem('275UserId')}`)
            .then((response) => {
                this.setState({
                    allProducts: response.data,
                    isFetched: true
                })
            })
            .catch((error) => {
                if (error.response.status === 409) {
                    this.setState({
                        allProducts: [],
                        isFetched: true
                    })
                } else {
                    this.setState({
                        isFetched: true
                    })
                }
            })
    }

    updateQuantity = (reqBody) => {
        axios.post(`/orders/update/cart`, reqBody)
            .then(() => {
                this.getAllProductsIncart()
            })
    }

    deleteQuantity = (reqParam) => {
        axios.delete(`/orders/removeProductFromCart?userId=${reqParam.userId}&orderItemId=${reqParam.orderItemId}`)
            .then(() => {
                this.getAllProductsIncart()
            })
    }

    render() {

        if (this.state.isFetched === false) {
            return (
                <div />
            )
        }
        if (this.state.allProducts.length === 0) {
            return (
                <p className="display-4 p-5 text-center">There are no products in your cart</p>
            )
        }

        let productsInCart = [],
            cartSubTotal = 0,
            slNo = 1

        for (var productObj of this.state.allProducts) {
            cartSubTotal = cartSubTotal + (productObj.product.price * productObj.quantity);
            productsInCart.push(<CartProduct slNo={slNo} productObj={productObj} editable={this.props.editable} update={this.updateQuantity} delete={this.deleteQuantity} />);
            slNo++;
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
                    <div className="col-md-1">Total</div>
                </div>
                {productsInCart}
                <div className="row font-weight-bold bg-secondary p-2 text-white text-center">
                    <div className="col-md-3 offset-md-6">Sub Total</div>
                    <div className="col-md-2">${cartSubTotal.toFixed(2)}</div>
                </div>
                <div className="row font-weight-bold bg-secondary p-2 text-white text-center">
                    <div className="col-md-3 offset-md-6">Tax (9.25%)</div>
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

    componentWillReceiveProps(props) {
        this.setState({
            quantity: props.productObj.quantity
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

    updateOrder = () => {
        const reqBody = {
            userId: localStorage.getItem('275UserId'),
            orderItemId: this.props.productObj.id,
            quantity: this.state.quantity
        }
        this.props.update(reqBody);
    }

    removeProductFromCart = () => {
        const reqBody = {
            userId: localStorage.getItem('275UserId'),
            orderItemId: this.props.productObj.id
        }
        this.props.delete(reqBody);
    }

    render() {
        let total = this.props.productObj.product.price * this.state.quantity

        return (
            <div className="row border p-2 text-center">
                <div className="col-md-1">{this.props.slNo}</div>
                <div className="col-md-2"><img src={this.props.productObj.product.imageURL} alt="..." class="img-thumbnail" /></div>
                <div className="col-md-2">{this.props.productObj.product.productName}</div>
                <div className="col-md-1">{this.props.productObj.product.price.toFixed(2)} / {this.props.productObj.product.unit}</div>
                <div className="col-md-3">
                    <div className="row">
                        <div className="col-md-2 p-0"><button className="btn btn-danger w-100" onClick={this.decreaseQuantity}>-</button></div>
                        <div className="col-md-8"><input type="text" value={this.state.quantity} className="form-control" onChange={this.quantityChangeHandler} /></div>
                        <div className="col-md-2 p-0"><button className="btn btn-success w-100" onClick={this.increaseQuantity}>+</button></div>
                    </div>
                </div>
                <div className="col-md-1">{total.toFixed(2)}</div>
                <div className="col-md-1"><button className="btn btn-warning" onClick={this.updateOrder}>Update</button></div>
                <div className="col-md-1"><button className="btn btn-danger" onClick={this.removeProductFromCart}>Remove</button></div>
            </div>
        )
    }
}



//export ViewCart Component
export default ViewCart;