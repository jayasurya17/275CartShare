import React, { Component } from 'react';
import axios from 'axios';

import { Redirect } from 'react-router';
import PickupOtherOrders from './pickupOtherOrders';


class UserContribution extends Component {

    constructor() {
        super()
        this.state = {
            selfPickup: true,
            showOtherOrders: false,
            contributionCredit: null,
            warningMsg: "",
            errMsg: "",
            storeId: null,
            redirect: null,
            pendingOrders: [],
            orderId: null,
            showButton: true
        }
    }

    componentDidMount() {
        axios.get(`/user/${localStorage.getItem('275UserId')}`)
            .then((response) => {
                this.setState({
                    contributionCredit: response.data.contributionCredit,
                    warningMsg: ""
                })
            })
    }

    pickupTypeChangeHandler = (e) => {
        if (e.target.value === "true") {
            this.setState({
                selfPickup: true
            })
        } else {
            this.setState({
                selfPickup: false
            })
        }
    }

    submitOrder = () => {
        this.setState({
            warningMsg: "",
            errMsg: "",
            showButton: false
        })
        if (this.state.contributionCredit === null) {
            this.setState({
                warningMsg: "Please wait while we are fetching your contribution credit"
            })
        } else {
            const reqBody = {
                userId: localStorage.getItem('275UserId'),
                selfPickup: this.state.selfPickup
            }
            if (this.state.selfPickup === true) {
                this.setState({
                    warningMsg: "We are placing your order. Please be patient!"
                })
                axios.post(`/orders/confirmOrder`, reqBody)
                    .then((response1) => {
                            this.setState({
                                storeId: response1.data.store.id,
                                orderId: response1.data.id
                            })
                            axios.get(`/orders/pendingInPool?userId=${localStorage.getItem('275UserId')}&storeId=${response1.data.store.id}`)
                                .then((response) => {
                                    this.setState({
                                        pendingOrders: response.data,
                                        showOtherOrders: true,
                                        showButton: true
                                    })
                                })
                    })
                    .catch((error) => {
                        if (error.response) {
                            this.setState({
                                errMsg: error.response.data,
                                warningMsg: "",
                                showButton: true
                            })
                        } else {
                            this.setState({
                                errMsg: "An error occoured. Invalid data",
                                warningMsg: "",
                                showButton: true
                            })
                        }
                    })
                        

                    
            } else {
                if (this.state.contributionCredit <= -4) {
                    var option = window.confirm(`Are you sure? Your contribution status is low (${this.state.contributionCredit} points)`)
                    if (option === true) {
                        this.setState({
                            warningMsg: "We are placing your order. Please be patient!"
                        })
                        axios.post(`/orders/confirmOrder`, reqBody)
                            .then(() => {
                                this.setState({
                                    redirect: false
                                })
                            })
                    }
                } else {
                    this.setState({
                        warningMsg: "We are placing your order. Please be patient!"
                    })
                    axios.post(`/orders/confirmOrder`, reqBody)
                        .then(() => {
                            this.setState({
                                redirect: false
                            })
                        })

                }
            }

        }
    }

    redirectToPickupPage = () => {
        this.setState({
            redirect: true
        })
    }

    render() {

        if (this.state.redirect === true) {
            return (<Redirect to="/pooler/pickup" />)
        } else if (this.state.redirect === false) {
            return (<Redirect to="/pooler/past/orders" />)
        }
        if (this.state.showOtherOrders === true) {
            console.log(this.state.pendingOrders)
            if (this.state.pendingOrders.length === 0) {
                return (<p className="display-4 text-justify p-5">Your order has been recieved. There are no pending orders from other poolers to be picked up at the moment.</p>)
            } else {
                return (<PickupOtherOrders pendingOrders={this.state.pendingOrders} storeId={this.state.storeId} orderId={this.state.orderId} redirect={this.redirectToPickupPage} />)
            }
        }
        let background = ""
        if (this.state.contributionCredit <= -6) {
            background = "bg-danger"
        } else if (this.state.contributionCredit <= -4) {
            background = "bg-warning"
        } else if (this.state.contributionCredit !== null) {
            background = "bg-success"
        }
        let submitButton = []
        if (this.state.contributionCredit != null) {
            submitButton = [
                <div className="mt-5 mb-5 text-center">
                    <p className="text-warning text-center">{this.state.warningMsg}</p>
                    <p className="text-danger text-center">{this.state.errMsg}</p>
                    {this.state.showButton?<button className="btn btn-success w-50" onClick={this.submitOrder}>Submit order</button>:null}
                </div>
            ]
        }
        return (
            <div>
                <div className="pt-5 text-center row">
                    <div className="col-md-6 offset-md-3">
                        <select className="form-control" onChange={this.pickupTypeChangeHandler}>
                            <option value={true}>I will pick up the order</option>
                            <option value={false}>Let fellow pooler pick up and deliver within 2 days</option>
                        </select>
                    </div>
                </div>
                <div className="pt-2 row">
                    <div className="col-md-2 offset-md-4">Your contribution credit</div>
                    <div className="col-md-2 font-weight-bold">{this.state.contributionCredit}</div>
                </div>
                <div className="pt-2 row">
                    <div className="col-md-2 offset-md-4">Your contribution status</div>
                    <div className={`col-md-2 ${background}`}></div>
                </div>
                {submitButton}
            </div>
        )
    }
}
//export UserContribution Component
export default UserContribution;