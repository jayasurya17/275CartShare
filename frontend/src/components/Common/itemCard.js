import React, { Component } from 'react';
import axios from 'axios';


class Home extends Component {

    constructor() {
        super();
        this.state = {
            quantity: 0,
            errMsg: "",
            successMsg: "",
        }
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

    deleteProduct = (e) => {
        e.preventDefault();
        this.setState({
            errMsg: "",
            successMsg: ""
        })
        axios.delete(`/admin/delete/product?productId=${this.props.productObj.id}`)
        .then(() => {
            this.props.getAllProducts()
        })
        .catch(() => {
            this.setState({
                errMsg: "There are unfulfilled orders for this product. It cannot be deleted at the moment!",
                successMsg: ""
            })
        })
    }

    addToCart = () => {
        this.setState({
            successMsg: "",
            errMsg: ""
        })
        const reqBody = {
            userId: localStorage.getItem('275UserId'),
            storeId: this.props.productObj.store.id,
            productId: this.props.productObj.id,
            quantity: this.props.productObj.unit === "pc" ? 1 : this.state.quantity
        }
        axios.post(`/orders/add/cart`, reqBody)
            .then(() => {
                this.setState({
                    successMsg: "Added to cart",
                    errMsg: "",
                    quantity: 0
                })
            })
            .catch((err) => {
                this.setState({
                    successMsg: "",
                    errMsg: err.response.data
                })
            })
    }

    render() {

        let itemFunction = []
        if (this.props.isAdmin) {
            itemFunction = [
                <div className="row">
                    <div className="col-md-6">
                        <a href={`/admin/product/update/${this.props.productObj.id}`}>
                            <button className="btn btn-warning w-100">Update this product</button>
                        </a>
                    </div>
                    <div className="col-md-6">
                        <button className="btn btn-danger w-100" onClick={this.deleteProduct}>Delete this product</button>
                    </div>
                </div>
            ]
        } else if (this.props.showQuantity) {
            if (this.props.productObj.unit === "pc") {
                itemFunction = [
                    <div className="row">
                        <div className="col-md-4 p-0">
                            <input type="text" className="form-control" disabled value="1 (default)" />
                        </div>
                        <div className="col-md-5 offset-md-3">
                            <button className="btn btn-info" onClick={this.addToCart}>Add to cart</button>
                        </div>
                    </div>
                ]
            } else {
                itemFunction = [
                    <div className="row">
                        <div className="col-md-1 p-0">
                            <button className="btn btn-danger w-100" onClick={this.decreaseQuantity}>-</button>
                        </div>
                        <div className="col-md-4 p-0">
                            <input type="text" className="form-control" value={this.state.quantity} onChange={this.quantityChangeHandler} />
                        </div>
                        <div className="col-md-1 p-0">
                            <button className="btn btn-success" onClick={this.increaseQuantity}>+</button>
                        </div>
                        <div className="col-md-5 offset-md-1">
                            <button className="btn btn-info" onClick={this.addToCart}>Add to cart</button>
                        </div>
                    </div>
                ]
            }
        }
        return (
            <div className="shadow row m-3 p-3 rounded border">
                <div className="col-md-8">
                    <h1 className="font-weight-lighter">{this.props.productObj.productName}</h1>
                    <h6 className="font-weight-lighter">{this.props.productObj.description}</h6>
                    { 
                        this.props.showStore === true?
                        <h6>Store: <span className="font-weight-lighter">{this.props.productObj.store.storeName} (ID : {this.props.productObj.store.id})</span></h6>
                        : null
                    }
                    <h6>Brand: <span className="font-weight-lighter">{this.props.productObj.brand}</span></h6>
                    <h6>SKU: <span className="font-weight-lighter">{this.props.productObj.sku}</span></h6>
                    <h6>${this.props.productObj.price} / {this.props.productObj.unit}</h6>
                    {itemFunction}
                </div>
                <div className="col-md-4">
                    <img src={this.props.productObj.imageURL} alt="..." class="img-thumbnail" />
                    <p className="text-danger">{this.state.errMsg}</p>
                    <p className="text-success">{this.state.successMsg}</p>
                </div>
            </div>
        )
    }
}
export default Home;