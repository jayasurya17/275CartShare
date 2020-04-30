import React, { Component } from 'react';
import Header from '../../Common/header';
import Navbar from '../../Common/navbar';
import axios from 'axios';


class PickupOrders extends Component {

    constructor() {
        super()
        this.state = {
            allOrders: [],
            fetched: false
        }
    }

    componentDidMount() {
        axios.get(`/orders/ordersToPickup?userId=${localStorage.getItem('275UserId')}`)
            .then((response) => {
                this.setState({
                    fetched: true,
                    allOrders: response.data
                })
            })
            .catch(() => {
                this.setState({
                    fetched: true
                })
            })
    }

    render() {
        if (this.state.fetched === false) {
            return (
                <div>
                    <Header />
                    <Navbar />
                </div>
            )
        }
        if (this.state.allOrders.length === 0) {
            return (
                <div>
                    <Header />
                    <Navbar />

                    <p className="p-5 display-4 text-center">You do not have any orders waiting to be picked up</p>

                </div>
            )
        }

        let orders = [],
            slNo = 0,
            temp = []
        for (let order of this.state.allOrders) {
            temp.push(
                <div className="col-md-4">
                    <OrdersComponent slNo={slNo + 1} order={order} />
                </div>
            )
            slNo++
            if (slNo % 3 === 0) {
                orders.push(
                    <div className="row">
                        {temp}
                    </div>
                )
                temp = []
            }
        }
        if (temp.length !== 0) {
            orders.push(
                <div className="row">
                    {temp}
                </div>
            )
        }

        return (
            <div>
                <Header />
                <Navbar />
                {orders}
            </div>
        )
    }
}

class OrdersComponent extends Component {

    render() {
        let allProducts = []
        let subTotal = 0
        let price
        for (let product of this.props.order) {
            price = product.productPrice * product.quantity
            subTotal += price
            allProducts.push(
                <div className="row p-2 border-left border-right">
                    <div className="col-md-3"><img src={product.productImage} alt="..." class="img-thumbnail" /></div>
                    <div className="col-md-3">{product.productName}</div>
                    <div className="col-md-1">{product.quantity}</div>
                    <div className="col-md-3">{product.productPrice} / {product.productUnit}</div>
                    <div className="col-md-2">{price}</div>
                </div>
            )
        }
        let tax = subTotal * 0.0925,
            convenienceFee = subTotal * 0.005,
            total = subTotal + tax + convenienceFee

        return (
            <div className="p-3">
                <div className="row p-2 bg-secondary text-white">
                    <div className="col-md-2"><h5># {this.props.order[0].orders.id}</h5></div>
                    <div className="col-md-6"><h5>Store: {this.props.order[0].orders.store.storeName}</h5></div>
                    <div className="col-md-4"><button className="btn btn-warning w-100">Show QR</button></div>
                </div>
                <div className="row p-2 bg-secondary text-white">
                    <div className="col-md-3 offset-md-3">Name</div>
                    <div className="col-md-1">Qty</div>
                    <div className="col-md-3">Cost</div>
                    <div className="col-md-2">Price</div>
                </div>
                {allProducts}
                <div className="row font-weight-bold bg-secondary p-2 text-white text-center">
                    <div className="col-md-6 offset-md-3">Sub Total</div>
                    <div className="col-md-3">${subTotal.toFixed(2)}</div>
                </div>
                <div className="row font-weight-bold bg-secondary p-2 text-white text-center">
                    <div className="col-md-6 offset-md-3">Tax (9.25%)</div>
                    <div className="col-md-3">${tax.toFixed(2)}</div>
                </div>
                <div className="row font-weight-bold bg-secondary p-2 text-white text-center">
                    <div className="col-md-6 offset-md-3">Convenience fee (0.5%)</div>
                    <div className="col-md-3">${convenienceFee.toFixed(2)}</div>
                </div>
                <div className="row font-weight-bold bg-secondary p-2 text-white text-center">
                    <div className="col-md-6 offset-md-3">Total</div>
                    <div className="col-md-3">${total.toFixed(2)}</div>
                </div>
            </div>
        )
    }
}

//export PickupOrders Component
export default PickupOrders;