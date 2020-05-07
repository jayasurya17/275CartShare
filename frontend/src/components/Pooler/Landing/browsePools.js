import React, { Component } from 'react'
import '../../../css/browsePool.css'
import PoolCard from './poolCard'
import axios from 'axios'

class Home extends Component {
	constructor(props) {
		super(props)
		this.state = {
			allPools: [],
			isFetched: false,
			searchPool: '',
			refreshSearch: []
		}
	}

	componentWillMount = () => {
		axios
			.get('/pool/getAllPools')
			.then(response => {
				if (response.status === 200) {
					console.log(response.data)
					this.setState({
						allPools: response.data,
						successMsg: 'All Pool Fetched',
						isFetched: true,
						refreshSearch: response.data
					})
				}
			})
			.catch(error => {
				this.setState({
					errMsg: 'No Pools Fetched',
					isFetched: true
				})
			})
	}
	searchTextChangeHandler = e => {
		this.setState({
			searchPool: e.target.value
		})
	}

	search = e => {
		e.preventDefault()

		if (this.state.searchPool !== "") {
			let filteredPools = []
			for (var pool of this.state.refreshSearch) {
				if (
					pool.zipcode.startsWith(this.state.searchPool) || 
					pool.neighborhoodName.startsWith(this.state.searchPool) || 
					pool.poolName.startsWith(this.state.searchPool)
				) {
					filteredPools.push(pool)
				}
			}
			this.setState({
				allPools: filteredPools
			})
		} else {
			this.setState({
				allPools: this.state.refreshSearch
			})
		}
	}

	clearSearch = e => {
		this.setState({
			allPools: this.state.refreshSearch,
			searchPool: ""
		})
	}

	render() {
		if (this.state.isFetched === false) {
			return (
				<div>
					<div className="row">
						<div className="col-md-8">
							<input className='form-control' onChange={this.searchTextChangeHandler} value={this.state.searchPool} placeholder='Search by pool name or neighborhood name or zipcode' />
						</div>
						<div className="col-md-2">
							<button className='form-control btn btn-warning w-100' onClick={this.search}>Search </button>
						</div>
						<div className="col-md-2">
							<button className='form-control btn btn-info w-100' onClick={this.clearSearch}>Clear </button>
						</div>
					</div>
					<div className='scrollable'>
						<p>Fetching...</p>
					</div>
				</div>
			)
		}

		let poolCards
		if (this.state.allPools.length > 0) {
			poolCards = this.state.allPools.map(pool => {
				return (
					<PoolCard
						poolName={pool.poolName}
						description={pool.description}
						leader={pool.pooler}
						zip={pool.zipcode}
						poolId={pool.id}
						alphaNumericId={pool.poolId}
						neighborhoodName={pool.neighborhoodName}
					/>
				)
			})
		}

		return (
			<div>
				<div className="row">
					<div className="col-md-8">
						<input className='form-control' onChange={this.searchTextChangeHandler} value={this.state.searchPool} placeholder='Search by pool name or neighborhood name or zipcode' />
					</div>
					<div className="col-md-2">
						<button className='form-control btn btn-warning w-100' onClick={this.search}>Search </button>
					</div>
					<div className="col-md-2">
						<button className='form-control btn btn-info w-100' onClick={this.clearSearch}>Clear </button>
					</div>
				</div>
				<div className='scrollable'>
					{poolCards ? poolCards : <p>No Pools Available</p>}
				</div>
			</div>
		)
	}
}

//export Home Component
export default Home
