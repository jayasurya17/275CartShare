import React, { Component } from 'react';

class UserDetails extends Component {

    constructor() {
        super()
        this.state = {
            nickName: "",
            screenName: ""
        }
    }

    nickNameChangeHandler = (e) => {
        this.setState({
            nickName: e.target.value
        })
    }

    screenNameChangeHandler = (e) => {
        this.setState({
            screenName: e.target.value
        })
    }

    render() {
        return (
            <div>
                <p className="display-1 text-center pt-5 mt-5">Your account has been verified</p>
                <h5 className="text-center mt-5 font-weight-light">Please enter your details before we can continue</h5>


                <div className="row text-center mt-5">
                    <div className="col-md-4 offset-md-4">
                        <div className="form-group">
                            <label>Screen name</label>
                            <input className="form-control" type="text" value={this.state.screenName} onChange={this.screenNameChangeHandler} placeholder="Screen name has to be numeric" />
                        </div>
                        <div className="form-group">
                            <label>Nick name</label>
                            <input className="form-control" type="text" value={this.state.nickName} onChange={this.nickNameChangeHandler} />
                        </div>
                        <button className="btn btn-success w-50" onClick={this.updateInformation}>Update details</button>
                    </div>
                </div>

            </div>
        )
    }
}
//export UserDetails Component
export default UserDetails;