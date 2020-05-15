import React, { Component } from 'react'
import Header from '../../Common/header'
import Navbar from '../../Common/navbar'
import AssociatedOrders from './associatedOrders'
import axios from 'axios'
import QRCode from 'qrcode.react'

class PickupOrders extends Component {
	constructor() {
		super()
		this.state = {
			allOrders: [],
			fetched: false,
		}
	}

	componentDidMount() {
		this.fetchAllOrders();
	}

	fetchAllOrders = () => {
		axios.get(`/orders/ordersToPickup?userId=${localStorage.getItem('275UserId')}`)
			.then(response => {
				this.setState({
					fetched: true,
					allOrders: response.data
				})
				console.log(this.state.allOrders)
			})
			.catch(() => {
				this.setState({
					fetched: true
				})
			})
	}


	render() {
		if (this.state.fetched === false) {
			return (
				<div>
					<Header />
					<Navbar />

					<p className='p-5 display-4 text-center'>Fetching...</p>
				</div>
			)
		}
		if (this.state.allOrders.length === 0) {
			return (
				<div>
					<Header />
					<Navbar />

					<p className='p-5 display-4 text-center'>You do not have any orders waiting to be picked up</p>
				</div>
			)
		}

        let orders = [],
            slNo = 0,
            row1 = [],
            row2 = [],
            row3 = [],
            rowNumber,
            orderObj
        for (slNo in this.state.allOrders) {
            rowNumber = slNo % 3
            orderObj = this.state.allOrders[slNo]
            if (rowNumber === 0) {
                row1.push(<OrdersComponent order={orderObj} update={this.fetchAllOrders}/>)
            } else if (rowNumber === 1) {
                row2.push(<OrdersComponent order={orderObj} update={this.fetchAllOrders}/>)
            } else {
                row3.push(<OrdersComponent order={orderObj} update={this.fetchAllOrders}/>)
            }
        }
        orders.push(
            <div className="row m-2">
                <div className="col-md-4">{row1}</div>
                <div className="col-md-4">{row2}</div>
                <div className="col-md-4">{row3}</div>
            </div>
        )

		return (
			<div>
				<Header />
				<Navbar />
				{orders}
			</div>
		)
	}
}

class OrdersComponent extends Component {


	constructor() {
		super()
		this.state = {
			associatedOrders: [],
			isFetched: false,
			isProcessing: false,
		}
	}

	componentDidMount() {
		axios.get(`/orders/associatedOrders?orderId=${this.props.order[0].orders.id}`)
			.then((response) => {
				this.setState({
					associatedOrders: response.data,
					isFetched: true,
					isProcessing: false,
				})
			})
			.catch(() => {
				this.setState({
					isFetched: true,
					isProcessing: false,
				})
			})
	}

	componentWillReceiveProps = (props) => {
		this.setState({
			isProcessing: false,
		})
		axios.get(`/orders/associatedOrders?orderId=${props.order[0].orders.id}`)
			.then((response) => {
				this.setState({
					associatedOrders: response.data,
					isFetched: true,
				})
			})
			.catch(() => {
				this.setState({
					isFetched: true,
				})
			})
	}

	handleScanQR = () => {
		this.setState({
			isProcessing: true
		})
		var orderId = this.props.order[0].orders.id;
		axios.get('/orders/pickUp/'.concat(orderId))
			.then((res) => {
				if (res.status === 200) {
					if(res.data === "Associated")
						alert("The order and its associated orders have been marked as picked up, and an email has been sent to you regarding the delivery instructions");
					else
						alert("Your order has been marked as pickep up");
					this.props.update();
				}
			})
			.catch((err) => {
				alert(err.response.data);
			})
	}

	render() {

		let associatedOrders = []
		let showQRcode = []
		let scanQRcode = []
		if (this.state.isFetched === false) {
			associatedOrders.push(
				<h3 className="font-weight-light text-center">Fetching all your associated orders and generating QR code..</h3>
			)
		} else if (this.state.associatedOrders.length === 0) {
			showQRcode.push(
				<QRCode value={`You are picking up order #${this.props.order[0].orders.id}`} />
			)
			scanQRcode.push(
				<button className='btn btn-success' onClick={this.handleScanQR} type='button' data-dismiss='modal'>Scan QR code</button>
			)
			associatedOrders.push(
				<h3 className="font-weight-light text-center">There are no associated orders</h3>
			)
		} else {
			var text = `You are picking up order #${this.props.order[0].orders.id} and associated order(s)`
			for (var order of this.state.associatedOrders) {
				associatedOrders.push(
					<AssociatedOrders order={order} />
				)
				text += ` #${order[0].orders.id}`
			}
			showQRcode.push(
				<QRCode value={text} />
			)
			scanQRcode.push(
				<button className='btn btn-success' onClick={this.handleScanQR} type='button' data-dismiss='modal'>Scan QR code</button>
			)
		}

		let checkoutButton = []
		if (this.state.isProcessing === true) {
			checkoutButton.push(
				<p className="text-warning">Processing...</p>
			)
		} else {
			checkoutButton.push(
				<button key={this.props.order[0].orders.id} type='button' className='btn btn-warning w-100' data-target={'#modalCenter' + this.props.order[0].orders.id} data-toggle='modal' >Checkout</button>
			)
		}

		let allProducts = []
		let subTotal = 0
		let price
		for (let product of this.props.order) {
			price = product.productPrice * product.quantity
			subTotal += price
			allProducts.push(
				<div className='row p-2 border-left border-right'>
					<div className='col-md-3'>
						<img src={product.productImage} alt='...' class='img-thumbnail' />
					</div>
					<div className='col-md-3'>{product.productName}</div>
					<div className='col-md-1'>{product.quantity}</div>
					<div className='col-md-3'>
						{product.productPrice} / {product.productUnit}
					</div>
					<div className='col-md-2'>{price.toFixed(2)}</div>
				</div>
			)
		}
		let tax = subTotal * 0.0925,
			convenienceFee = subTotal * 0.005,
			total = subTotal + tax + convenienceFee
		return (
			<div className='p-3'>
				<div className='row p-2 bg-secondary text-white'>
					<div className='col-md-2'>
						<h5># {this.props.order[0].orders.id}</h5>
					</div>
					<div className='col-md-6'>
						<h5>Store: {this.props.order[0].orders.store.storeName}</h5>
					</div>
					<div className='col-md-4'>
						{checkoutButton}
					</div>
				</div>
				<div className='row p-2 bg-secondary text-white'>
					<div className='col-md-3 offset-md-3'>Name</div>
					<div className='col-md-1'>Qty</div>
					<div className='col-md-3'>Cost</div>
					<div className='col-md-2'>Price</div>
				</div>
				{allProducts}
				<div className='row font-weight-bold bg-secondary p-2 text-white text-center'>
					<div className='col-md-6 offset-md-3'>Sub Total</div>
					<div className='col-md-3'>${subTotal.toFixed(2)}</div>
				</div>
				<div className='row font-weight-bold bg-secondary p-2 text-white text-center'>
					<div className='col-md-6 offset-md-3'>Tax (9.25%)</div>
					<div className='col-md-3'>${tax.toFixed(2)}</div>
				</div>
				<div className='row font-weight-bold bg-secondary p-2 text-white text-center'>
					<div className='col-md-6 offset-md-3'>Convenience fee (0.5%)</div>
					<div className='col-md-3'>${convenienceFee.toFixed(2)}</div>
				</div>
				<div className='row font-weight-bold bg-secondary p-2 text-white text-center'>
					<div className='col-md-6 offset-md-3'>Total</div>
					<div className='col-md-3'>${total.toFixed(2)}</div>
				</div>



				{/* <!-- Modal --> */}
				<div className='modal fade' id={'modalCenter' + this.props.order[0].orders.id} tabIndex='-1' role='dialog' aria-labelledby='modalCenterTitle' aria-hidden='true' >
					<div className='modal-dialog modal-dialog-centered' role='document'>
						<div className='modal-content'>
							<div className='modal-header'>
								<h5 className='modal-title text-center' id='modalCenterTitle'>QR CODE</h5>
								<button type='button' className='close' data-dismiss='modal' aria-label='Close' >
									<span aria-hidden='true'>&times;</span>
								</button>
							</div>
							<div className='modal-body'>
								<div align='center'>
									{showQRcode}
								</div>
								<div>
									{associatedOrders}
								</div>
							</div>
							<div className='modal-footer'>
								{scanQRcode}
								<button type='button' className='btn btn-secondary' data-dismiss='modal'>Close</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		)
	}
}

//export PickupOrders Component
export default PickupOrders