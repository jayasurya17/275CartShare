import React, { Component } from 'react';
import axios from 'axios';
import constants from '../../../utils/constants';


class UserContribution extends Component {

    constructor() {
        super()
        this.state = {
            selfPickup: true,
            showOtherOrders: false,
            contributionCredit: null,
            warningMsg: ""
        }
    }

    componentDidMount() {
        axios.get(`${constants.BACKEND_SERVER.URL}/user/${localStorage.getItem('275UserId')}`)
            .then((response) => {
                this.setState({
                    contributionCredit: response.data.contributionCredit,
                    warningMsg: ""
                })
            })
    }

    pickupTypeChangeHandler = (e) => {
        this.setState({
            selfPickup: e.target.value
        })
    }

    submitOrder = () => {
        if (this.state.contributionCredit === null) {
            this.setState({
                warningMsg: "Please wait while we are fetching your contribution credit"
            })
        } else {
            if (this.state.selfPickup === true) {
                this.setState({
                    showOtherOrders: true
                })
            } else {
                if (this.state.contributionCredit <= -4) {
                    window.confirm("Are you sure?")
                }
            }
            const reqBody = {
                userId: localStorage.getItem('275UserId'),
                selfPickup: this.state.selfPickup
            }
            axios.post(`${constants.BACKEND_SERVER.URL}/orders/confirmOrder`, reqBody)
        }
    }

    render() {

        let background = ""
        if (this.state.contributionCredit <= -6) {
            background = "bg-danger"
        } else if (this.state.contributionCredit <= -4) {
            background = "bg-warning"
        } else if (this.state.contributionCredit !== null) {
            background = "bg-success"
        }
        return (
            <div>
                <div className="pt-5 text-center row">
                    <div className="col-md-6 offset-md-3">
                        <select className="form-control" onChange={this.pickupTypeChangeHandler}>
                            <option value={true}>I will pick up the order</option>
                            <option value={false}>Let fellow pooler pick up the order</option>
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
                <div className="mt-5 mb-5 text-center">
                    <p className="text-warning text-center">{this.state.warningMsg}</p>
                    <button className="btn btn-success w-50" onClick={this.submitOrder}>Submit order</button>
                </div>
            </div>
        )
    }
}
//export UserContribution Component
export default UserContribution;