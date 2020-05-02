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
        axios.get('/orders/deliver/'.concat(localStorage.getItem('275UserId')))
        .then((res) => {
            if(res.status === 200){
                console.log('delivery', res.data);
                this.setState({
                    fetched: true,
                    allOrders: res.data
                });   
            }
        })
        .catch((err) => {
            alert(err.response.data);
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
                    <OrdersComponent slNo={slNo + 1} order={order}/>
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

    markDelivered = () => {
        var orderId = this.props.order[0].orders.id;
        axios.put('/orders/deliver/'.concat(orderId), null, null)
        .then((res) => {
            if(res.status === 200){
                alert('The order has been marked as delivered');
                window.location.reload(false);
            }
        })
        .catch((err) => {
            alert(err.response.data);
        })
    }

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

        var addr = this.props.order[0].orders.user.address;

        return (
            <div className="p-3 border">
                <div className="row p-2 bg-secondary text-white">
                    <div className="col-md-12">
                        <h5><span className="font-weight-light">Picked up from: </span>{this.props.order[0].orders.store.storeName}</h5>
                        <h5><span className="font-weight-light">Deliver to: </span>{this.props.order[0].orders.user.screenName}</h5>
                        <h5>{`${addr.street}, ${addr.city}, ${addr.state}, ${addr.zipcode}`}</h5>
                    </div>
                </div>
                <div className="row p-2 bg-secondary text-white">
                    <div className="col-md-4"><h5>Order #{this.props.order[0].orders.id}</h5></div>
                    <div className="col-md-6 offset-md-2"><button className="btn btn-success w-100" onClick={this.markDelivered}>Mark as delivered</button></div>
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