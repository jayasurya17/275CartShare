import React, { Component } from 'react';
import Header from '../../Common/header';
import Navbar from '../../Common/navbar';
import axios from 'axios';


class PastOrders extends Component {

    constructor() {
        super()
        this.state = {
            allOrders: [],
            fetched: false
        }
    }

    componentDidMount() {
        this.fetchAllOrders();
        // Set state as fetched when you get response
        
    }

    fetchAllOrders = () => {
        axios.get('/orders/pastOrders/'.concat(localStorage.getItem('275UserId')))
        .then((res) => {
            if (res.status === 200) {
                this.setState({
                    fetched: true,
                    allOrders: res.data
                })
            }
        })
        .catch((err) => {
            this.setState({
                fetched: true,
                allOrders: []
            })
            alert(err.response.data);
        })
    }

    render() {
        if (this.state.fetched === false) {
            return (
                <div>
                    <Header />
                    <Navbar />

                    <p className="p-5 display-4 text-center">Fetching...</p>
                </div>
            )
        }
        if (this.state.allOrders.length === 0) {
            return (
                <div>
                    <Header />
                    <Navbar />

                    <p className="p-5 display-4 text-center">You do not have any past orders</p>

                </div>
            )
        }

        let orders = [],
            slNo = 0,
            temp = []
        for (let order of this.state.allOrders) {
            temp.push(
                <div className="col-md-4">
                    <OrdersComponent order={order} update={this.fetchAllOrders}/>
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

    markAsNotDelivered = () => {
        axios.put('/orders/notDelivered/'.concat(this.props.order[0].orders.id), null, null)
        .then((res) => {
            if(res.status === 200){
                alert(res.data);
                this.props.update();
            }
        })
    }

    render() {

        let allProducts = []
        let subTotal = 0
        let price
        for (let product of this.props.order) {
            price = product.productPrice
            subTotal += price
            allProducts.push(
                <div className="row p-2 border-left border-right">
                    <div className="col-md-3"><img src={product.productImage} alt="..." class="img-thumbnail" /></div>
                    <div className="col-md-3">{product.productName}</div>
                    <div className="col-md-1">{product.quantity}</div>
                    <div className="col-md-3">3.2 / KG</div>
                    <div className="col-md-2">{price}</div>
                </div>
            )
        }
        let tax = subTotal * 0.0925,
            convenienceFee = subTotal * 0.005,
            total = subTotal + tax + convenienceFee
        let status = []
        let statusOfOrder = this.props.order[0].orders.status;
        var myId = this.props.order[0].orders.user.id;
        var pickUpPoolerId = 0;
        if (this.props.order[0].orders.pickupPooler !== null) {
            pickUpPoolerId = this.props.order[0].orders.pickupPooler.id;
        }
        else {
            pickUpPoolerId = null;
        }
        if (statusOfOrder === "Delivered" && pickUpPoolerId !== null && myId !== pickUpPoolerId) {
            status.push(
                <div className="row p-2 border">
                    <div className="col-md-4"><h5>Order # {this.props.order[0].orders.id}</h5></div>
                    <div className="col-md-6 offset-md-2"><button className="btn btn-success w-100" onClick={this.markAsNotDelivered}>Mark as not delivered</button></div>
                </div>
            )
            status.push(
                <div className="row p-2 bg-secondary text-white">
                    <div className="col-md-12">
                        <h5><span className="font-weight-light">Picked up from: </span>{this.props.order[0].orders.store.storeName}</h5>
                        <h5><span className="font-weight-light">Deliver By: </span>{this.props.order[0].orders.pickupPooler.screenName}</h5>
                    </div>
                </div>
            )
        } else if (statusOfOrder === "Delivered" && pickUpPoolerId != null && myId === pickUpPoolerId) {
            status.push(
                <div className="row p-2 border">
                    <div className="col-md-4"><h5>Order # {this.props.order[0].orders.id}</h5></div>
                    <div className="col-md-8"><h5 className="text-success">Picked up from {this.props.order[0].orders.store.storeName}</h5></div>
                </div>
            )
        } else if (statusOfOrder === "Ordered" || pickUpPoolerId == null) {
            status.push(
                <div className="row p-2 border">
                    <div className="col-md-4"><h5>Order # {this.props.order[0].orders.id}</h5></div>
                    <div className="col-md-8"><h5 className="text-warning">Waiting to be picked up by fellow pooler</h5></div>
                </div>
            )
        } else if (statusOfOrder === "Confirmed" && pickUpPoolerId != null && myId !== pickUpPoolerId) {
            status.push(
                <div className="row p-2 border">
                    <div className="col-md-4"><h5>Order # {this.props.order[0].orders.id}</h5></div>
                    <div className="col-md-8"><h5 className="text-info">A fellow pooler will be picking up this order soon</h5></div>
                </div>
            )
        }
        else if (statusOfOrder === "Confirmed" && pickUpPoolerId != null && myId === pickUpPoolerId) {
            status.push(
                <div className="row p-2 border">
                    <div className="col-md-4"><h5>Order # {this.props.order[0].orders.id}</h5></div>
                    <div className="col-md-8"><h5 className="text-info">Order to be picked up from {this.props.order[0].orders.store.storeName}</h5></div>
                </div>
            )
        } else if (statusOfOrder === "PickedUp") {
            status.push(
                <div className="row p-2 border">
                    <div className="col-md-4"><h5>Order # {this.props.order[0].orders.id}</h5></div>
                    <div className="col-md-8"><h5 className="text-success">A fellow pooler has picked up this order</h5></div>
                </div>
            )
        } else if (statusOfOrder === "NotDelivered") {
            status.push(
                <div className="row p-2 border">
                    <div className="col-md-4"><h5>Order # {this.props.order[0].orders.id}</h5></div>
                    <div className="col-md-8"><h5 className="text-danger">This order has been marked as not delivered</h5></div>
                </div>
            )
        } else if (statusOfOrder === "Cancelled") {
            status.push(
                <div className="row p-2 border">
                    <div className="col-md-4"><h5>Order # {this.props.order[0].orders.id}</h5></div>
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