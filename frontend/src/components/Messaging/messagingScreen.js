import React, { Component } from 'react'
import axios from 'axios'
import Header from '../Common/header'
import Navbar from '../Common/navbar'

class Message extends Component {
	constructor() {
		super()
		this.state = {
			screenName: '',
			message: '',
			found: true,
			receiverId: '',
			result: '',
			showButton: true
		}
	}
	changehandler = e => {
		const val = e.target.value
		this.setState({
			[e.target.name]: val
		})
	}
	sendMessage = async e => {
		e.preventDefault()
		if (this.state.screenName === '' && this.state.message === '') {
			alert('Please provide required details')
		} else {
			this.setState({
				result: "Sending message",
				showButton: false
			})

			await axios
				.post(
					'/message/' +
					localStorage.getItem('275UserName') +
					'/' +
					this.state.screenName,
					null,
					{
						params: {
							message: this.state.message
						}
					}
				)
				.then(response => {
					if (response.status === 200) {
						console.log('sent')
						this.setState({
							result: 'Message sent to ' + this.state.screenName,
							message: '',
							screenName: '',
							found: true,
							receiverId: '',
							showButton: true
						})
					}
				})

				.catch(error => {
					if (error.response) {
						this.setState({
							found: false,
							result: error.response.data,
							showButton: true
						})
					}
				})
		}
	}

	render() {
		return (
			<div>
				<Header></Header>
				<Navbar></Navbar>
				<div className="row">

					<div class='card col-md-6 offset-md-3 p-5 text-center border-info mb-3'>
						<div class='card-header form-control-lg '>
							Send a message to another user
          </div>
						<div class='card-body'>
							<h5 class='card-title text-left form-control-sm'>
								Screen name and Message
            </h5>
							<form>
								<div class='input-group mb-3'>
									<div class='input-group-prepend'>
										<span class='input-group-text' id='basic-addon1'>
											@
                  </span>
									</div>
									<input
										name='screenName'
										type='text'
										class='form-control form-control-lg'
										onChange={this.changehandler}
										value={this.state.screenName}
										placeholder='Screen Name'
										aria-label='Screen Name'
										aria-describedby='basic-addon1'
									/>
								</div>

								<div class='input-group mb-3'>
									<div class='input-group-prepend'>
										<span class='input-group-text' id='basic-addon2'>
											Message
                  </span>
									</div>
									<input
										name='message'
										type='text-area'
										class='form-control form-control-lg'
										onChange={this.changehandler}
										value={this.state.message}
										placeholder='Message'
										aria-label='Message'
										aria-describedby='basic-addon2'
									/>
								</div>

								<p>{this.state.result}</p>
								{
									this.state.showButton ?
										<button
											type='submit'
											name='send'
											class='btn btn-primary'
											onClick={this.sendMessage}
										>
											Send Message
              						</button> : null
								}
							</form>
						</div>
					</div>

				</div>
			</div>
		)
	}
}
export default Message
