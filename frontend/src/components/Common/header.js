import React, { Component } from 'react'
import '../../css/header.css'

class Home extends Component {
	render() {
		if (this.props.isAdmin) {
			return (
				<div className='row pl-3 pt-1 pb-1 stickyHeader bg-light text-secondary'>
					<div className='col-md-8'>
						<h1 className='display-4'>CartShare</h1>
					</div>
					<div className='col-md-2 offset-md-2'>
						<div className='row pt-3'>
							<div className='col-md-12 text-center'>
								<p className='text-center'>
									Not {localStorage.getItem('275NickName')}?{' '}
									<a href='/logout' className='text-decoration-none'>Logout</a>
								</p>
							</div>
						</div>
					</div>
				</div>
			)
		}

		if (this.props.isLanding) {
			return (
				<div className='row pl-3 pt-1 pb-1 stickyHeader bg-light text-secondary'>
					<div className='col-md-8'>
						<a href='/pooler/browse' className='text-decoration-none text-dark'>
							<h1 className='display-4'>CartShare</h1>
						</a>
					</div>
					<div className='col-md-2 offset-md-2'>
						<div className='row pt-3'>
							<div className='col-md-12 text-center'>
								<p className='text-center'>
									Not {localStorage.getItem('275NickName')}?{' '}
									<a href='/logout' className='text-decoration-none'>Logout</a>
								</p>
							</div>
						</div>
					</div>
				</div>
			)
		}

		return (
			<div className='row pl-3 pt-1 pb-1 stickyHeader bg-light text-secondary'>
				<div className='col-md-8'>
					<a href='/pooler/browse' className='text-decoration-none text-dark'>
						<h1 className='display-4'>CartShare</h1>
					</a>
				</div>

				<div className='col-md-2'>
					<div className='row'>
						<div className='col-md-12 text-center'>
							<a href='/pooler/message'>
								<button className='btn btn-outline-primary w-100'>Send message</button>
							</a>
						</div>
					</div>
				</div>
				<div className='col-md-2'>
					<div className='row'>
						<div className='col-md-12 text-center'>
							<a href='/pooler/view/cart'>
								<button className='btn btn-outline-success w-50'>View Cart</button>
							</a>
						</div>
					</div>
					<div className='row'>
						<div className='col-md-12 text-center'>
							<p className='text-center'>
								Not {localStorage.getItem('275NickName')}?{' '}
								<a href='/logout' className='text-decoration-none'>Logout</a>
							</p>
						</div>
					</div>
				</div>
			</div>
		)
	}
}
//export Home Component
export default Home
