import React, { Component } from 'react';
import Header from '../../Common/header';
import Navigation from '../../Common/navbar';
import SupportReferrals from './supportReferrals';
import AcceptRequests from './acceptRequests';

class UserInfo extends Component {

    render() {


        let background = "bg-success"
        if (this.props.userObj.contributionCredit <= -6) {
            background = "bg-danger"
        } else if (this.props.userObj.contributionCredit <= -4) {
            background = "bg-warning"
        }

        return (
            <div className="row p-2">
                <div className="col-md-2">{this.props.userObj.name}</div>
                <div className="col-md-2">{this.props.userObj.nickname}</div>
                <div className="col-md-6">{`${this.props.userObj.address.street}, ${this.props.userObj.address.city}, ${this.props.userObj.address.state} - ${this.props.userObj.address.zipcode}`}</div>
                <div className="col-md-1">{this.props.userObj.contributionCredit}</div>
                <div className={`col-md-1 ${background}`}></div>
            </div>
        )
    }
}

class ViewDetails extends Component {

    constructor() {
        super()
        this.state = {
            pool: {
                name: null,
                neighbourhood: null,
                description: null,
                zipcode: null,
                coordinator: {
                    id: null,
                    name: null
                }
            }
        }
    }

    render() {

        let userObj = {
            name: "Name",
            nickname: "NickName",
            address: {
                street: "1334 The Alameda",
                city: "San Jose",
                state: "CA",
                zipcode: "95126"
            },
            contributionCredit: 10
        }
        let allUsers = []
        for (var i = 0; i < 10; i++) {
            allUsers.push(<UserInfo slNo={i + 1} userObj={userObj} />)
        }
        let viewRequests,
            updateInfo
        if (localStorage.getItem('275UserId') === this.state.pool.coordinator.id) {
            viewRequests = <AcceptRequests />
            updateInfo =
                <div className="text-center pt-3">
                    <button className="w-50 btn btn-warning">Update pool information</button>
                </div>
        } else if (this.state.pool.coordinator.id != null) {
            viewRequests = <SupportReferrals />
        }

        return (
            <div>
                <Header />
                <Navigation />
                <div className="p-5">
                    <div className="row">
                        <div className="col-md-6">
                            <p className="display-4 text-center">Pool Name</p>
                            <h1>Neighbourhood: <span className="font-weight-light">San Jose</span></h1>
                            <h1>Description: <span className="font-weight-light">A pool description</span></h1>
                            <h1>Zipcode: <span className="font-weight-light">95126</span></h1>
                            <h1>Coordinator: <span className="font-weight-light">Name of Coordinator</span></h1>
                            {updateInfo}
                        </div>
                        <div className="col-md-6">
                            <p className="display-4 text-center">Current members</p>
                            <div className="row p-2 bg-secondary text-white font-weight-bold">
                                <div className="col-md-2">ScreenName</div>
                                <div className="col-md-2">Nickname</div>
                                <div className="col-md-6">Address</div>
                                <div className="col-md-2">Contribution</div>
                            </div>
                            {allUsers}
                        </div>
                    </div>
                    {viewRequests}
                </div>
            </div>
        )
    }

}

export default ViewDetails;