import React, { Component } from 'react'
import { Redirect } from 'react-router'
import axios from 'axios'
import Header from '../../Common/header'
import CreatePool from './createPool'
import BrowsePools from './browsePools'

class Home extends Component {
	constructor(props) {
		super(props)
		this.state = {
			userId: localStorage.getItem('275UserId'),
			poolMember: false,
			redURL: '',
			isFetched: false
		}
	}

	componentWillMount = () => {
		axios
			.get('/poolMembers/getPoolByUser/' + this.state.userId)
			.then(response => {
				if (response.status === 200) {
					localStorage.setItem('isMember', true)
					this.setState({
						poolMember: true,
						redURL: '/pooler/view/pool',
						isFetched: true
					})
				} else {
					localStorage.setItem('isMember', false)
					this.setState({
						poolMember: false,
						redURL: '',
						isFetched: true
					})
				}
			})
			.catch(() => {
				localStorage.setItem('isMember', false)
				this.setState({
					poolMember: false,
					redURL: '',
					isFetched: true
				})
			})
	}
	render() {
		if (this.state.poolMember === true) {
			return <Redirect to={this.state.redURL} />
		} else if (this.state.isFetched === false) {
			return (
				<div>
					<Header isLanding={true} />
				</div>
			)
		}

		return (
			<div>
				<Header isLanding={true} />
				<p className='display-4 text-center pt-5'>What would you like to do?</p>
				<div className='row'>
					<div className='col-md-6 border-right pl-5 pr-5'>
						<CreatePool />
					</div>
					<div className='col-md-6 border-left pl-5 pr-5'>
						<BrowsePools />
					</div>
				</div>
			</div>
		)
	}
}
//export Home Component
export default Home
