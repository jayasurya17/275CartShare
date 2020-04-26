import React, { Component } from 'react';
import axios from 'axios';
import constants from '../../../utils/constants';

class PickupOtherOrders extends Component {

    constructor() {
        super()
        this.state = {
            numberOfOrdersToPickup: 1
        }
    }

    numberOfOrdersToPickupChangeHandler = (e) => {
        this.setState({
            numberOfOrdersToPickup: e.target.value
        })
    }

    submitPickUp = () => {
        const reqBody = {
            userId: localStorage.getItem('275UserId'),
            storeId: this.props.storeId,
            numberOfOrders: this.state.numberOfOrdersToPickup
        }
        axios.post(`${constants.BACKEND_SERVER.URL}/orders/pickupOtherOrders`, reqBody)
            .then((response) => {
                window.alert(response.data);
                this.props.redirect()
            })
    }

    render() {

        let otherOrders = [],
            slNo = 0,
            numberOfOrders = [],
            temp = []
        for (let order of this.props.pendingOrders) {
            numberOfOrders.push(<option value={slNo + 1}>{slNo + 1}</option>)
            temp.push(
                <div className="col-md-4">
                    <OtherOrders slNo={slNo + 1} order={order} />
                </div>
            )
            slNo++
            if (slNo % 3 === 0) {
                otherOrders.push(
                    <div className="row">
                        {temp}
                    </div>
                )
                temp = []
            }
        }
        if (temp.length !== 0) {
            otherOrders.push(
                <div className="row">
                    {temp}
                </div>
            )
        }
        return (
            <div className="p-5">
                <h4 className="font-weight-light text-center">Number of orders you would like to pickup for your fellow poolers</h4>
                <div className="row">
                    <div className="col-md-3 offset-md-4">
                        <select className="form-control" value={this.state.numberOfOrdersToPickup} onChange={this.numberOfOrdersToPickupChangeHandler}>
                            {numberOfOrders}
                        </select>
                    </div>
                    <div className="col-md-1">
                        <button className="btn btn-success w-100" onClick={this.submitPickUp}>Submit</button>
                    </div>
                </div>
                {otherOrders}
            </div>
        )
    }

}

class OtherOrders extends Component {

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
                    <div className="col-md-3"><h5>Order: {this.props.slNo}</h5></div>
                    <div className="col-md-4">Name</div>
                    <div className="col-md-2">Brand</div>
                    <div className="col-md-2">Quantity</div>
                </div>
                {allProducts}
            </div>
        )
    }
}

export default PickupOtherOrders;