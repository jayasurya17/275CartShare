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
			editName: "",
			editDescription: "",
			editNeighborhood: ""
		};
	}

	changeHandler = (e) => {
		this.setState({
			[e.target.name]: e.target.value,
		});
	};

	componentDidMount = () => {
		axios.get("/poolMembers/getPoolByUser/" + this.state.userId)
			.then((response) => {
				if (response.status === 200) {
					console.log(response.data);
					this.setState({
						poolDetails: response.data.pool,
						poolReceived: true,
					});
					if (response.data.pool) {
						this.setState({
							editName: response.data.pool.poolName,
							editDescription: response.data.pool.description,
							editNeighborhood: response.data.pool.neighborhoodName
						})
					}
					this.fetchPoolMembers();
				} else {
					console.log(response.data);
					this.setState({
						poolReceived: false,
					});
				}
			})
			.catch(() => {
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
			.catch(() => {
				this.setState({
					membersReceived: true,
				});
			});
	};

	isAlpha = (value) => {
		return value !== null && value.match(/^[a-zA-Z]+$/) !== null;
	}

	editPoolInformation = () => {
		if (this.state.editName === "" || this.state.editNeighborhood === "") {
			alert("Please provide require Name and Neighborhood Details")
		} else if (this.isAlpha(this.state.editName) !== true) {
			alert("Name can be text only")
		} else {
			console.log({
				id: this.state.poolDetails.id,
				description: this.state.editDescription,
				neighborhoodName: this.state.editNeighborhood,
				poolName: this.state.editName,
				zipcode: this.state.poolDetails.zipcode,
				poolerId: this.state.poolDetails.pooler.id
			});
			axios.put("/pool/editPool", null, {
				params: {
					id: this.state.poolDetails.id,
					description: this.state.editDescription,
					neighborhoodName: this.state.editNeighborhood,
					poolName: this.state.editName,
					zipcode: this.state.poolDetails.zipcode,
					poolerId: this.state.poolDetails.pooler.id
				}
			})
				.then(response => {
					if (response.status === 200) {
						console.log(response.data);
						this.setState({
							successMsgModal: "Pool Details Updated Successfully",
							errorMsgModal: ""
						})
					} else {
						this.setState({
							successMsgModal: "",
							errorMsgModal: "Pool Details could not be updated. Try again!"
						})
					}
					window.location.reload()
				})
				.catch(error => {
					if (error.response) {
						this.setState({
							successMsgModal: "",
							errorMsgModal: error.response.data
						})
					} else {
						this.setState({
							successMsgModal: "",
							errorMsgModal: "An error occoured. Invalid data"
						})
					}
				})
		}
	}

	leavePool = () => {
		axios.delete(`/poolMembers/leavePool?userId=${localStorage.getItem('275UserId')}&poolId=${this.state.poolDetails.id}`)
			.then(() => {
				localStorage.setItem('isMember', false)
				window.location.reload()
			})
			.catch((err) => {
				alert(err.response.data)
			})
	}

	deletePool = () => {
		axios.delete(`/pool/deletePool?userId=${localStorage.getItem('275UserId')}&poolId=${this.state.poolDetails.id}`)
			.then(() => {
				localStorage.setItem('isMember', false)
				window.location.reload()
			})
			.catch((err) => {
				alert(err.response.data)
			})
	}

	render() {
		let poolMembers = [];
		let requests = [];
		if (this.state.membersReceived) {
			for (var i = 0; i < this.state.members.length; i++) {
				poolMembers.push(<UserInfo key={i + 1} slNo={i + 1} userObj={this.state.members[i]} />);
			}
		}
		if (this.state.poolReceived) {
			if (Number(localStorage.getItem("275UserId")) === Number(this.state.poolDetails.pooler.id)) {
				requests.push(
					<div key="update" className="row pt-5">
						<div className="col-md-3 offset-md-2">
							<button className="w-100 btn btn-warning" data-toggle="modal" data-target={"#ModalCenter"}>Update pool information</button>
						</div>
						<div className="col-md-3 offset-md-2">
							<button className="w-100 btn btn-danger" onClick={this.deletePool}>Delete pool</button>
						</div>
					</div>
				);
				requests.push(
					<AcceptRequests key={this.state.userId} userId={this.state.userId} screenName={this.state.userScreenName} poolDetails={this.state.poolDetails} update={this.fetchPoolMembers} />
				);
			} else {
				requests.push(
					<div className="text-center pt-5">
						<button className="w-25 btn btn-danger" onClick={this.leavePool}>Leave pool</button>
					</div>
				);
				requests.push(
					<SupportReferrals key={this.state.userId} userId={this.state.userId} screenName={this.state.userScreenName} poolDetails={this.state.poolDetails} update={this.fetchPoolMembers} />
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
								<p className="display-4 text-center">{this.state.poolDetails.poolName}</p>
								<h1>
									Pool ID:{" "}<span className="font-weight-light">{this.state.poolDetails.poolId}</span>
								</h1>
								<h1>
									Neighbourhood:{" "}<span className="font-weight-light">{this.state.poolDetails.neighborhoodName}</span>
								</h1>
								<h1>
									Description:{" "}<span className="font-weight-light">{this.state.poolDetails.description}</span>
								</h1>
								<h1>
									Zipcode:{" "}<span className="font-weight-light">{this.state.poolDetails.zipcode}</span>
								</h1>
								<h1>
									Coordinator:{" "}<span className="font-weight-light">{this.state.poolDetails.pooler.screenName}</span>
								</h1>
							</div>
						) : (
								<p className="p-5 display-4">Fetching pool details...</p>
							)}
						{this.state.membersReceived ? (
							<div className="col-md-6">
								<p className="display-4 text-center">Current members</p>
								<div className="row p-2 bg-secondary text-white font-weight-bold">
									<div className="col-md-2">ScreenName</div>
									<div className="col-md-2">Nickname</div>
									<div className="col-md-6">Address</div>
									<div className="col-md-2">Contribution</div>
								</div>
								{poolMembers.length > 0 ? poolMembers : null}
							</div>
						) : this.state.poolReceived ? (
							<p className="p-5 display-4">Fetching pool members...</p>
						) : null}
					</div>
					{requests}
					<div className="modal fade" id={"ModalCenter"} tabIndex="-1" role="dialog" aria-labelledby="modalCenterTitle" aria-hidden="true" >
						<div className="modal-dialog modal-dialog-centered" role="document" >
							<div className="modal-content">
								<div className="modal-header">
									<h5 className="modal-title" id="modalCenterTitle" >Update Pool Information</h5>
									<button type="button" className="close" data-dismiss="modal" aria-label="Close" >
										<span aria-hidden="true">&times;</span>
									</button>
								</div>
								<div className="modal-body">
									{this.state.errorMsgModal ? (
										<p className="text-danger text-center">
											{this.state.errorMsgModal}
										</p>
									) : null}
									{this.state.successMsgModal ? (
										<p className="text-success text-center">
											{this.state.successMsgModal}
										</p>
									) : null}
									<div className="form-group">
										<label>Name</label>
										<input type="text" className="form-control" placeholder="Name" value={this.state.editName} onChange={this.changeHandler} name="editName" required />
									</div>
									<div className="form-group">
										<label>Neighborhood</label>
										<input type="text" className="form-control" placeholder="Neighborhood" value={this.state.editNeighborhood} onChange={this.changeHandler} name="editNeighborhood" required />
									</div>
									<div className="form-group">
										<label>Description</label>
										<input type="text" className="form-control" placeholder="Description" value={this.state.editDescription} onChange={this.changeHandler} name="editDescription" />
									</div>
								</div>
								<div className="modal-footer">
									<button type="button" className="btn btn-secondary" data-dismiss="modal">Close</button>
									<button type="button" className="btn btn-primary" onClick={this.editPoolInformation}>Send request</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		);
	}
}

export default ViewDetails;
