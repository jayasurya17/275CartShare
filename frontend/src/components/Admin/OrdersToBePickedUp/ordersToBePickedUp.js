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
        axios.get(`/orders/ordersToPickup`)
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
                    <Header isAdmin={true} />
                    <Navbar isAdmin={true} />
                </div>
            )
        }
        if (this.state.allOrders.length === 0) {
            return (
                <div>
                    <Header isAdmin={true} />
                    <Navbar isAdmin={true} />

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
                <Header isAdmin={true} />
                <Navbar isAdmin={true} />
                {orders}
            </div>
        )
    }
}

class OrdersComponent extends Component {

    render() {

        let allProducts = []
        for (let product of this.props.order) {
            allProducts.push(
                <div className="row p-2">
                    <div className="col-md-3"><img src={product.productImage} alt="..." class="img-thumbnail" /></div>
                    <div className="col-md-4">{product.productName}</div>
                    <div className="col-md-2">{product.productBrand}</div>
                    <div className="col-md-2">{product.quantity}</div>
                </div>
            )
        }

        return (
            <div className="p-3">
                <div className="row p-2 bg-secondary text-white">
                    <div className="col-md-2"><h5># {this.props.order[0].orders.id}</h5></div>
                    <div className="col-md-6"><h5>Store: {this.props.order[0].orders.store.storeName}</h5></div>
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

//export PickupOrders Component
export default PickupOrders;