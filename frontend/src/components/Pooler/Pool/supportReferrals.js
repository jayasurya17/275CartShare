import React, { Component } from 'react';
// import Header from '../../Common/header';
// import Navigation from '../../Common/navbar';

class UserInfo extends Component {

    render() {
        return (
            <div className="row p-2">
                <div className="col-md-1">{this.props.slNo}</div>
                <div className="col-md-2">{this.props.userObj.name}</div>
                <div className="col-md-2">{this.props.userObj.nickname}</div>
                <div className="col-md-5">{`${this.props.userObj.address.street}, ${this.props.userObj.address.city}, ${this.props.userObj.address.state} - ${this.props.userObj.address.zipcode}`}</div>
                <div className="col-md-1"><button className="btn btn-success w-100">Support</button></div>
                <div className="col-md-1"><button className="btn btn-danger w-100">Reject</button></div>
            </div>
        )
    }
}

class SupportReferral extends Component {

    render() {

        let userObj = {
            name: "Name",
            nickname: "NickName",
            address: {
                street: "1334 The Alameda",
                city: "San Jose",
                state: "CA",
                zipcode: "95126"
            }
        }
        let allUsers = []
        for (var i = 0; i < 10; i++) {
            allUsers.push(<UserInfo slNo={i + 1} userObj={userObj} />)
        }

        return (
            <div>
                {/* <Header />
                <Navigation /> */}
                <div className="p-5">
                    <p className="display-4 text-center">Active requests</p>
                    <div className="row p-2 bg-secondary text-white font-weight-bold">
                        <div className="col-md-1">Sl No</div>
                        <div className="col-md-2">ScreenName</div>
                        <div className="col-md-2">Nickname</div>
                        <div className="col-md-5">Address</div>
                    </div>
                    {allUsers}
                </div>
            </div>
        )
    }

}

export default SupportReferral;