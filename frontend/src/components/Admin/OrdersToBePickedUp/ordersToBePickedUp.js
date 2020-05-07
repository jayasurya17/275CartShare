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
            row1 = [],
            row2 = [],
            row3 = [],
            rowNumber,
            orderObj
        for (slNo in this.state.allOrders) {
            rowNumber = slNo % 3
            orderObj = this.state.allOrders[slNo]
            if (rowNumber === 0) {
                row1.push(<OrdersComponent order={orderObj} update={this.fetchAllOrders}/>)
            } else if (rowNumber === 1) {
                row2.push(<OrdersComponent order={orderObj} update={this.fetchAllOrders}/>)
            } else {
                row3.push(<OrdersComponent order={orderObj} update={this.fetchAllOrders}/>)
            }
        }
        orders.push(
            <div className="row m-2">
                <div className="col-md-4">{row1}</div>
                <div className="col-md-4">{row2}</div>
                <div className="col-md-4">{row3}</div>
            </div>
        )

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
                <div className="row p-2 border">
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