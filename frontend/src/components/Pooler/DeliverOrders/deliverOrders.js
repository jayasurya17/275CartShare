import React, { Component } from 'react';
import Header from '../../Common/header';
import Navbar from '../../Common/navbar';
import axios from 'axios';


class DeliverOrders extends Component {

    constructor() {
        super()
        this.state = {
            allOrders: [1, 2, 3, 4],
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

                    <p className="p-5 display-4 text-center">You do not have any orders that have to be delivered</p>

                </div>
            )
        }

        let orders = [],
            slNo = 0,
            temp = []
        for (let order of this.state.allOrders) {
            temp.push(
                <div className="col-md-4">
                    <OrdersComponent />
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
        for (let product of ["productObj", "productObj"]) {
            allProducts.push(
                <div className="row p-2">
                    <div className="col-md-3"><img src="https://toppng.com/uploads/preview/clipart-free-seaweed-clipart-draw-food-placeholder-11562968708qhzooxrjly.png" alt="..." class="img-thumbnail" /></div>
                    <div className="col-md-4">Product Name</div>
                    <div className="col-md-2">Brand</div>
                    <div className="col-md-2">8</div>
                </div>
            )
        }


        return (
            <div className="p-3 border">
                <div className="row p-2 bg-secondary text-white">
                    <div className="col-md-12">
                        <h5><span className="font-weight-light">Picked up from: </span>Store Name</h5>
                        <h5><span className="font-weight-light">Deliver to: </span>Jayasurya</h5>
                        <h5>1334 The Alameda, San Jose, CA - 95126</h5>
                    </div>
                </div>
                <div className="row p-2 bg-secondary text-white">
                    <div className="col-md-4"><h5>Order # 291</h5></div>
                    <div className="col-md-6 offset-md-2"><button className="btn btn-success w-100">Mark as delivered</button></div>
                </div>
                <div className="row p-2 bg-secondary text-white">
                    {/* <div className="col-md-3"><h5>Order: {this.props.slNo}</h5></div> */}
                    <div className="col-md-4 offset-md-3">Name</div>
                    <div className="col-md-2">Brand</div>
                    <div className="col-md-2">Quantity</div>
                </div>
                {allProducts}
            </div>
        )
    }
}

//export DeliverOrders Component
export default DeliverOrders;