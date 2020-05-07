import React, { Component } from "react";
import axios from "axios";
class UserInfo extends Component {

	acceptRequest = () => {
		const reqParams = {
			poolMemberId: this.props.userObj.id,
			status: "Accepted",
			requestId: this.props.reqId
		}
		this.props.manageRequest(reqParams)
	}

	rejectRequest = () => {
		const reqParams = {
			poolMemberId: this.props.userObj.id,
			status: "Rejected",
			requestId: this.props.reqId
		}
		this.props.manageRequest(reqParams)
	}

	render() {
		return (
			<div className="row p-2">
				<div className="col-md-1">{this.props.slNo}</div>
				<div className="col-md-2">{this.props.userObj.screenName}</div>
				<div className="col-md-2">{this.props.userObj.nickName}</div>
				{this.props.userObj.address === null ? (
					<div className="col-md-5">Address Not Available</div>
				) : (
						<div className="col-md-5">{`${this.props.userObj.address.street}, ${this.props.userObj.address.city}, ${this.props.userObj.address.state} - ${this.props.userObj.address.zipcode}`}</div>
					)}

				<div className="col-md-1">
					<button className="btn btn-success w-100" onClick={this.acceptRequest}>Accept</button>
				</div>
				<div className="col-md-1">
					<button className="btn btn-danger w-100" onClick={this.rejectRequest}>Reject</button>
				</div>
			</div>
		);
	}
}

class SupportReferral extends Component {
	constructor(props) {
		super(props);
		this.state = {
			userId: this.props.userId,
			screenName: this.props.screenName,
			poolDetails: this.props.poolDetails,
			requests: []
		};
	}

	componentWillMount() {
		this.getDetails()
	};

	getDetails = () => {

		axios.get("/poolMembers/requestedRequests", {
			params: {
				screenName: this.state.screenName,
				isLeader: "true",
			},
		})
			.then((response) => {
				const data = response.data.filter((request) => {
					return request.status !== "Accepted";
				});
				this.setState({
					requests: data,
					requestsReceived: true,
				});
			})
			.catch((error) => {
				console.log(error.response.data);
				this.setState({
					requestsReceived: true,
					requests: []
				});
			});
	}

	manageRequest = (reqParams) => {
		axios.put(`/poolMembers/manageRequest?poolId=${this.state.poolDetails.id}&poolMemberId=${reqParams.poolMemberId}&status=${reqParams.status}&requestId=${reqParams.requestId}`)
			.then(() => {
				this.getDetails()
				this.props.update()
			})
			.catch(() => {
				alert(`Error in changing the status to ${reqParams.status}`)
				this.getDetails()
				this.props.update()
			})
	}

	render() {

		let allUsers = [];
		if (this.state.requestsReceived) {
			// console.log(this.state.requests);
			for (var i = 0; i < this.state.requests.length; i++) {
				allUsers.push(
					<UserInfo
						key={i + 1}
						slNo={i + 1}
						userObj={this.state.requests[i].member}
						reqId={this.state.requests[i].id}
						manageRequest={this.manageRequest}
					/>
				);
			}
			if (this.state.requests.length === 0) {
				return (
					<div className="p-5">
						<p className="display-4 text-center">There are no active requests</p>
					</div>
				);
			}
		} else {
			return (null);
		}

		return (
			<div className="p-5">
				<p className="display-4 text-center">Active requests</p>
				<div className="row p-2 bg-secondary text-white font-weight-bold">
					<div className="col-md-1">Sl No</div>
					<div className="col-md-2">Name</div>
					<div className="col-md-2">Nickname</div>
					<div className="col-md-5">Address</div>
				</div>
				{allUsers}
			</div>
		);
	}
}

export default SupportReferral;
