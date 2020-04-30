import React, { Component } from "react";
import "../../../css/browsePool.css";
import PoolCard from "./poolCard";
import axios from "axios";

class Home extends Component {
	constructor(props) {
		super(props);
		this.state = {
			allPools: [],
		};
	}

	componentWillMount = () => {
		axios
			.get("/pool/getAllPools")
			.then((response) => {
				if (response.status === 200) {
					console.log(response.data);
					this.setState({
						allPools: response.data,
						successMsg: "All Pool Fetched",
					});
				}
			})
			.catch((error) => {
				this.setState({
					errMsg: "No Pools Fetched",
				});
			});
	};

	render() {
		let poolCards;
		if (this.state.allPools.length > 0) {
			poolCards = this.state.allPools.map((pool) => {
				return (
					<PoolCard
						poolName={pool.poolName}
						description={pool.description}
						leader={pool.pooler}
						zip={pool.zipcode}
						poolId={pool.id}
					/>
				);
			});
		}

		return (
			<div>
				<input
					className="form-control"
					placeholder="Search by pool name or neighborhood name or zipcode"
				/>
				<div className="scrollable">
					{poolCards ? poolCards : <p>No Pools Available</p>}
				</div>
			</div>
		);
	}
}

//export Home Component
export default Home;
