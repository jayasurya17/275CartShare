import React, { Component } from "react";
import Header from "../../Common/header";
import Navigation from "../../Common/navbar";
import AcceptRequests from "./acceptRequests";
import SupportReferrals from "./supportReferrals";
import axios from "axios";

class UserInfo extends Component {
	render() {
		let background = "bg-success";
		if (this.props.userObj.contributionCredit <= -6) {
			background = "bg-danger";
		} else if (this.props.userObj.contributionCredit <= -4) {
			background = "bg-warning";
		}

		return (
			<div className="row p-2">
				<div className="col-md-2">{this.props.userObj.screenName}</div>
				<div className="col-md-2">{this.props.userObj.nickName}</div>
				{this.props.userObj.address === null ? (
					<div className="col-md-6">Address Not Available</div>
				) : (
					<div className="col-md-6">{`${this.props.userObj.address.street}, ${this.props.userObj.address.city}, ${this.props.userObj.address.state} - ${this.props.userObj.address.zipcode}`}</div>
				)}
				<div className="col-md-1">
					{this.props.userObj.contributionCredit}
				</div>
				<div className={`col-md-1 ${background}`}></div>
			</div>
		);
	}
}

class ViewDetails extends Component {
	constructor() {
		super();
		this.state = {
			userId: localStorage.getItem("275UserId"),
			userScreenName: localStorage.getItem("275UserName"),
			userEmail: localStorage.getItem("275UserEmail"),
			pool: {
				name: null,
				neighbourhood: null,
				description: null,
				zipcode: null,
				coordinator: {
					id: null,
					name: null,
				},
			},
		};
	}

	componentDidMount = () => {
		axios.get("/poolMembers/getPoolByUser/" + this.state.userId)
			.then((response) => {
				if (response.status === 200) {
					console.log(response.data);
					this.setState({
						poolDetails: response.data.pool,
						poolReceived: true,
					});
					this.fetchPoolMembers();
				} else {
					console.log(response.data);
					this.setState({
						poolReceived: false,
					});
				}
			})
			.catch((error) => {
				alert(error.response.data);
				this.setState({
					poolReceived: false,
				});
			});
	};

	fetchPoolMembers = () => {
		axios.get("/poolMembers/viewPoolMembers", {
			params: {
				poolId: this.state.poolDetails.id,
			},
		})
		.then((response) => {
			if (response.status === 200) {
				let members = response.data.map((member) => {
					return member.member;
				});
				this.setState({
					members: members,
					membersReceived: true,
				});
			} else {
				this.setState({
					membersReceived: true,
				});
			}
		})
		.catch((error) => {
			this.setState({
				membersReceived: true,
			});
		});
	}

	render() {
		let poolMembers = [];
		let requests = [];
		if (this.state.membersReceived) {
			for (var i = 0; i < this.state.members.length; i++) {
				poolMembers.push(
					<UserInfo key={i+1} slNo={i + 1} userObj={this.state.members[i]} />
				);
			}
		}
		if (this.state.poolReceived) {
			if (
				Number(localStorage.getItem("275UserId")) ===
				Number(this.state.poolDetails.pooler.id)
			) {
				// if(false) {
				requests.push(
					<div key="update" className="text-center pt-5">
						<button className="w-50 btn btn-warning">
							Update pool information
						</button>
					</div>
				);
				requests.push(
					<AcceptRequests
						key={this.state.userId}
						userId={this.state.userId}
						screenName={this.state.userScreenName}
						poolDetails={this.state.poolDetails}
						update={this.fetchPoolMembers} 
					/>
				);
			} else {
				requests.push(
					<SupportReferrals
						key={this.state.userId}
						userId={this.state.userId}
						screenName={this.state.userScreenName}
						poolDetails={this.state.poolDetails}
						update={this.fetchPoolMembers} 
					/>
				);
			}
		}

		return (
			<div>
				<Header />
				<Navigation />
				<div className="p-5">
					<div className="row">
						{this.state.poolReceived ? (
							<div className="col-md-6">
								<p className="display-4 text-center">
									{this.state.poolDetails.poolName}
								</p>
								<h1>
									Neighbourhood:{" "}
									<span className="font-weight-light">
										{
											this.state.poolDetails
												.neighborhoodName
										}
									</span>
								</h1>
								<h1>
									Description:{" "}
									<span className="font-weight-light">
										{this.state.poolDetails.description}
									</span>
								</h1>
								<h1>
									Zipcode:{" "}
									<span className="font-weight-light">
										{this.state.poolDetails.zipcode}
									</span>
								</h1>
								<h1>
									Coordinator:{" "}
									<span className="font-weight-light">
										{
											this.state.poolDetails.pooler
												.screenName
										}
									</span>
								</h1>
								{/* {updateInfo} */}
							</div>
						) : null}
						<div className="col-md-6">
							<p className="display-4 text-center">
								Current members
							</p>
							<div className="row p-2 bg-secondary text-white font-weight-bold">
								<div className="col-md-2">ScreenName</div>
								<div className="col-md-2">Nickname</div>
								<div className="col-md-6">Address</div>
								<div className="col-md-2">Contribution</div>
							</div>
							{poolMembers.length > 0 ? poolMembers : null}
						</div>
					</div>
					{requests}
				</div>
			</div>
		);
	}
}

export default ViewDetails;
