import React, { Component } from 'react';

class UserContribution extends Component {

    constructor() {
        super()
        this.state = {
            selfPickup: true,
            showOtherOrders: false,
            contributionCredit: -5
        }
    }

    pickupTypeChangeHandler = (e) => {
        console.log(e.target.value)
        this.setState({
            selfPickup: e.target.value
        })
    }

    submitOrder = () => {
        console.log(this.state.selfPickup)
        if (this.state.selfPickup === true) {
            this.setState({
                showOtherOrders: true
            })
        } else {
            if (this.state.contributionCredit <= -4) {
                window.confirm("Are you sure?")
            }
            this.props.submitOrder();
        }
    }

    render() {

        let background = "bg-success"
        if (this.state.contributionCredit <= -6 ) {
            background = "bg-danger"
        } else if (this.state.contributionCredit <= -4) {
            background = "bg-warning"
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
                    <button className="btn btn-success w-50" onClick={this.submitOrder}>Submit order</button>
                </div>
            </div>
        )
    }
}
//export UserContribution Component
export default UserContribution;