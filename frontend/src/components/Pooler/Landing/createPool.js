import React, { Component } from "react";
import axios from "axios";
import { Redirect } from 'react-router';

class Home extends Component {
	constructor(props) {
		super(props);
		this.state = {
			userId: localStorage.getItem("275UserId"),
			poolId: "",
			poolName: "",
			neighborhood: "",
			description: "",
			zipcode: "",
			redirect: ""
		};
		this.changeHandler = this.changeHandler.bind(this);
	}

	changeHandler = (e) => {
		this.setState({
			[e.target.name]: e.target.value,
		});
	};

	isEmpty = (value) => {
		if (value.trim().localeCompare("") === 0) {
			return true;
		}
		return false;
	};

	isValidZipCode = (zipcode) => {
		if (zipcode.length !== 5) {
			return false;
		}
		for (var value of zipcode) {
			if (isNaN(parseInt(value, 10))) {
				return false;
			}
		}
		return true;
	};

	isAlphanumeric = (value) => {
		return value !== null && value.match(/^[a-zA-Z0-9]+$/) !== null;
	}

	isAlpha = (value) => {
		return value !== null && value.match(/^[a-zA-Z]+$/) !== null;
	}

	areValidValues = () => {
		if (this.isEmpty(this.state.poolId)) return false;
		if (this.isEmpty(this.state.poolName)) return false;
		if (this.isEmpty(this.state.neighborhood)) return false;
		if (this.isEmpty(this.state.description)) return false;
		if (this.isEmpty(this.state.zipcode)) return false;
		return true;
	};

	onSubmit = (e) => {
		e.preventDefault();
		console.log("Submitting Pool Details");

		if (this.isValidZipCode(this.state.zipcode) === false) {
			this.setState({
				successMsg: "",
				errMsg: "Invalid zipcode",
			});
		} else if (this.isAlphanumeric(this.state.poolId) !== true) {
			this.setState({
				successMsg: "",
				errMsg: "Pool ID must be alphanumeric"
			})
		} else if (this.isAlpha(this.state.poolName) !== true) {
			this.setState({
				successMsg: "",
				errMsg: "Pool name can be text only"
			})
		} else if (this.areValidValues()) {
			this.setState({
				successMsg: "",
				errMsg: ""
			})
			axios
				.post("/pool/createPool", null, {
					params: {
						poolName: this.state.poolName,
						neighborhoodName: this.state.neighborhood,
						description: this.state.description,
						zipcode: this.state.zipcode,
						poolerId: this.state.userId,
						poolId: this.state.poolId
					},
				})
				.then((response) => {
                    localStorage.setItem('isMember', true)
					this.setState({
						user: response.data,
						successMsg: "New Pool Created",
						errMsg: "",
						redirect: <Redirect to="/pooler/view/pool" />,
					});
				})
				.catch((error) => {
					if (error.response) {
						this.setState({
							successMsg: "",
							errMsg: error.response.data,
						});
					} else {
						this.setState({
							successMsg: "",
							errMsg: "An error occoured. Invalid data",
						});
					}
				});
		}
	};

	render() {
		return (
			<div>
				{this.state.redirect}
				<div className="form-group">
					<label>Pool ID</label>
					<input type="text" onChange={this.changeHandler} value={this.state.poolId} className="form-control" name="poolId" required />
				</div>
				<div className="form-group">
					<label>Pool name</label>
					<input type="text" onChange={this.changeHandler} value={this.state.poolName} className="form-control" name="poolName" required />
				</div>
				<div className="form-group">
					<label>Neighbourhood</label>
					<input type="text" onChange={this.changeHandler} value={this.state.neighborhood} className="form-control" name="neighborhood" required />
				</div>
				<div className="form-group">
					<label>Description</label>
					<input type="text" onChange={this.changeHandler} value={this.state.description} className="form-control" name="description" />
				</div>
				<div className="form-group">
					<label>Zipcode</label>
					<input type="text" onChange={this.changeHandler} value={this.state.zipcode} className="form-control" name="zipcode" required />
				</div>
				{this.state.successMsg ? <p className="text-success text-center">{this.state.successMsg}</p> : null}
				{this.state.errMsg ? <p className="text-danger text-center">{this.state.errMsg}</p> : null}
				<button type="submit" onClick={this.onSubmit} className="btn btn-success w-100"> Create a new pool</button>
			</div>
		);
	}
}
//export Home Component
export default Home;
