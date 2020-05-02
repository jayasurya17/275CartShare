import React, { Component } from 'react';
import Header from '../../Common/header';
import Navbar from '../../Common/navbar';
import axios from 'axios';


class PastOrders extends Component {

    constructor() {
        super()
        this.state = {
            allOrders: [1, 2, 3, 4, 5, 6],
            fetched: false
        }
    }

    componentDidMount() {

        // Set state as fetched when you get response
        this.setState({
            fetched: true
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

                    <p className="p-5 display-4 text-center">You do not have any orders that has to be delivered</p>

                </div>
            )
        }

        let orders = [],
            slNo = 0,
            temp = []
        for (let order of this.state.allOrders) {
            temp.push(
                <div className="col-md-4">
                    <OrdersComponent order={order} />
                </div>
            )
            slNo++
            if (slNo % 3 === 0) {
                orders.push(
                    <div className="row m-2">
                        {temp}
                    </div>
                )
                temp = []
            }
        }
        if (temp.length !== 0) {
            orders.push(
                <div className="row m-2">
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
        for (let product of ["productObj", "productObj"]) {
            price = 6.4
            subTotal += price
            allProducts.push(
                <div className="row p-2 border-left border-right">
                    <div className="col-md-3"><img src="https://toppng.com/uploads/preview/clipart-free-seaweed-clipart-draw-food-placeholder-11562968708qhzooxrjly.png" alt="..." class="img-thumbnail" /></div>
                    <div className="col-md-3">Product Name</div>
                    <div className="col-md-1">2</div>
                    <div className="col-md-3">3.2 / KG</div>
                    <div className="col-md-2">{price.toFixed(2)}</div>
                </div>
            )
        }
        let tax = subTotal * 0.0925,
            convenienceFee = subTotal * 0.005,
            total = subTotal + tax + convenienceFee
        let status = []
        let statusOfOrder = ""
        if (statusOfOrder === "Delivered by someone else") {
            status.push(
                <div className="row p-2 border">
                    <div className="col-md-4"><h5>Order # 291</h5></div>
                    <div className="col-md-6 offset-md-2"><button className="btn btn-success w-100">Mark as not delivered</button></div>
                </div>
            )
            status.push(
                <div className="row p-2 bg-secondary text-white">
                    <div className="col-md-12">
                        <h5><span className="font-weight-light">Picked up from: </span>Store Name</h5>
                        <h5><span className="font-weight-light">Deliver By: </span>Jayasurya</h5>
                    </div>
                </div>
            )
        } else if (statusOfOrder === "Was picked up by myself") {
            status.push(
                <div className="row p-2 border">
                    <div className="col-md-4"><h5>Order # 291</h5></div>
                    <div className="col-md-8"><h5 className="text-success">Picked up from Store Name</h5></div>
                </div>
            )
        } else if (statusOfOrder === "Waiting to be up by someone else. Nobody has requested to pickup the order yet.") {
            status.push(
                <div className="row p-2 border">
                    <div className="col-md-4"><h5>Order # 291</h5></div>
                    <div className="col-md-8"><h5 className="text-warning">Waiting to be picked up by fellow pooler</h5></div>
                </div>
            )
        } else if (statusOfOrder === "Waiting to be up by someone else from the store. Someone has requested to pickup but not taken it from the store") {
            status.push(
                <div className="row p-2 border">
                    <div className="col-md-4"><h5>Order # 291</h5></div>
                    <div className="col-md-8"><h5 className="text-info">A fellow pooler will be picking up this order soon</h5></div>
                </div>
            )
        } else if (statusOfOrder === "Fellow pooler has picked up the order from store") {
            status.push(
                <div className="row p-2 border">
                    <div className="col-md-4"><h5>Order # 291</h5></div>
                    <div className="col-md-8"><h5 className="text-success">A fellow pooler has picked up this order</h5></div>
                </div>
            )
        } else if (statusOfOrder === "Marked as not delivered") {
            status.push(
                <div className="row p-2 border">
                    <div className="col-md-4"><h5>Order # 291</h5></div>
                    <div className="col-md-8"><h5 className="text-danger">This order has been marked as not delivered</h5></div>
                </div>
            )
        } else if (statusOfOrder === "Cancelled") {
            status.push(
                <div className="row p-2 border">
                    <div className="col-md-4"><h5>Order # 291</h5></div>
                    <div className="col-md-8"><h5 className="text-danger">This order has been cancelled since nobody picked it up</h5></div>
                </div>
            )
        }


        return (
            <div className="p-3">
                {status}
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

//export PastOrders Component
export default PastOrders;