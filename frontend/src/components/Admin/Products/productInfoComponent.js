import React, { Component } from 'react'
import Select from 'react-select'
import axios from 'axios'

class StoreInfoComponent extends Component {
	constructor() {
		super()
		this.state = {
			SKU: '',
			name: '',
			description: '',
			brand: '',
			unit: '',
			price: '',
			errMsg: '',
			successMsg: '',
			selectedFile: '',
			filename: '',
			allStores: [],
			selectedStores: [],
			storeName: ''
		}
	}

	componentDidMount() {
		if (this.props.productId) {
			axios
				.get(`/product/get/details?productId=${this.props.productId}`)
				.then(response => {
					if (response.data.store) {
						this.setState({
							SKU: response.data.sku,
							name: response.data.productName,
							description: response.data.description,
							brand: response.data.brand,
							unit: response.data.unit,
							price: response.data.price,
							filename: '',
							storeName: response.data.store.storeName
						})
					}
				})
		} else if (this.props.storeId) {
			axios.get(`/store/details/${this.props.storeId}`).then(response => {
				this.setState({
					storeName: response.data.storeName,
					selectedStores: [{ value: response.data.id }]
				})
			})
		} else {
			axios
				.get(`/store/all?adminId=${localStorage.getItem('275UserId')}`)
				.then(response => {
					this.setState({
						allStores: response.data
					})
				})
		}
	}

	SKUChangeHandler = (e) => {
		this.setState({
			SKU: e.target.value
		})
	}

	nameChangeHandler = (e) => {
		this.setState({
			name: e.target.value
		})
	}

	descriptionChangeHandler = (e) => {
		this.setState({
			description: e.target.value
		})
	}

	brandChangeHandler = (e) => {
		this.setState({
			brand: e.target.value
		})
	}

	unitChangeHandler = (e) => {
		this.setState({
			unit: e.target.value
		})
	}

	priceChangeHandler = (e) => {
		this.setState({
			price: e.target.value
		})
	}

	onChangeMultiSelect = (opt) => {
		this.setState({
			selectedStores: opt
		})
	}

	fileUploadChangeHandler = (e) => {
		this.setState({
			selectedFile: e.target.files[0],
			filename: e.target.value
		})
	}

	isEmpty = (value) => {
		if (String(value).trim().localeCompare('') === 0) {
			return true
		}
		return false
	}

	areValidValues = () => {
		if (this.isEmpty(this.state.name)) return false
		if (this.isEmpty(this.state.description)) return false
		if (this.isEmpty(this.state.brand)) return false
		if (this.isEmpty(this.state.unit)) return false
		if (this.isEmpty(this.state.price)) return false
		if (this.state.selectedStores.length === 0) return false
		return true
	}

	areValidValuesForUpdating = () => {
		if (this.isEmpty(this.state.name)) return false
		if (this.isEmpty(this.state.description)) return false
		if (this.isEmpty(this.state.brand)) return false
		if (this.isEmpty(this.state.unit)) return false
		if (this.isEmpty(this.state.price)) return false
		return true
	}

	addProduct = () => {
		if (this.areValidValues()) {
			let storeIDs = []
			for (var store of this.state.selectedStores) {
				storeIDs.push(store.value)
			}

			let data = new FormData()
			data.set('userId', localStorage.getItem('275UserId'))
			data.set('storeIDs', storeIDs)
			data.set('productName', this.state.name)
			data.append('productImage', this.state.selectedFile)
			data.set('productDescription', this.state.description)
			data.set('productBrand', this.state.brand)
			data.set('productUnit', this.state.unit)
			data.set('price', this.state.price)
			data.set('sku', this.state.SKU)
			axios.post(`/admin/add/products`, data)
				.then(() => {
					if (this.props.getAllProducts) {
						this.props.getAllProducts()
					}
					this.setState({
						SKU: '',
						name: '',
						description: '',
						brand: '',
						unit: '',
						price: '',
						selectedStores: [],
						selectedFile: '',
						filename: '',
						successMsg: 'Added',
						errMsg: ''
					})
				})
				.catch(error => {
					if (error.response) {
						this.setState({
							successMsg: '',
							errMsg: error.response.data
						})
					} else {
						this.setState({
							successMsg: "",
							errMsg: "An error occured. File size maybe too big (512 KB limit)."
						})
					}
				})
		} else {
			this.setState({
				errMsg: 'Values cannot be empty',
				successMsg: ''
			})
		}
	}

	updateProduct = () => {
		if (this.areValidValuesForUpdating()) {

			let data = new FormData()
			data.set('userId', localStorage.getItem('275UserId'))
			data.set('productId', this.props.productId)
			data.set('productName', this.state.name)
			data.append('productImage', this.state.selectedFile)
			data.set('productDescription', this.state.description)
			data.set('productBrand', this.state.brand)
			data.set('productUnit', this.state.unit)
			data.set('productPrice', this.state.price)
			axios.put(`/admin/update/product`, data)
				.then(() => {
					this.setState({
						successMsg: 'Updated',
						errMsg: ''
					})
				})
				.catch((error) => {
					this.setState({
						successMsg: '',
						errMsg: "Error"
					})
				})
		} else {
			this.setState({
				errMsg: 'Values cannot be empty',
				successMsg: ''
			})
		}
	}


	render() {
		let storeDetails = [],
			SKUDetails = []
		if (this.props.productId) {
			storeDetails = [
				<div className='form-group'>
					<label>Store name</label>
					<input className='form-control' type='text' value={this.state.storeName} disabled />
				</div>
			]
			SKUDetails = [
				<div className='form-group'>
					<label>SKU</label>
					<input type='text' className='form-control' value={this.state.SKU} disabled />
				</div>
			]
		} else if (this.props.storeId) {
			storeDetails = [
				<div className='form-group'>
					<label>Store name</label>
					<input className='form-control' type='text' value={this.state.storeName} disabled />
				</div>
			]
			SKUDetails = [
				<div className='form-group'>
					<label>SKU</label>
					<input type='text' className='form-control' onChange={this.SKUChangeHandler} value={this.state.SKU} />
				</div>
			]
		} else {
			let allStores = []
			for (var store of this.state.allStores) {
				allStores.push({ label: store.storeName, value: store.id })
			}
			storeDetails = [
				<div className='form-group'>
					<label>Select Stores</label>
					<Select isMulti onChange={this.onChangeMultiSelect} options={allStores} value={this.state.selectedStores} />
				</div>
			]
			SKUDetails = [
				<div className='form-group'>
					<label>SKU</label>
					<input type='text' className='form-control' onChange={this.SKUChangeHandler} value={this.state.SKU} />
				</div>
			]
		}
		let action = (
			<button className='btn btn-success w-100' onClick={this.addProduct}>
				Add product
			</button>
		)
		if (this.props.productId) {
			action = (
				<button className='btn btn-warning w-100' onClick={this.updateProduct}>
					Update product details
				</button>
			)
		}

		return (
			<div className='pl-5 pr-5'>
				<div className='row'>
					<div className='col-md-3 offset-md-2 pt-5'>
						{SKUDetails}
						<div className='form-group'>
							<label>Name</label>
							<input type='text' className='form-control' onChange={this.nameChangeHandler} value={this.state.name} />
						</div>
						<div className='form-group'>
							<label>Description</label>
							<input type='text' className='form-control' onChange={this.descriptionChangeHandler} value={this.state.description} />
						</div>
						<div className='form-group'>
							<label>Brand</label>
							<input type='text' className='form-control' onChange={this.brandChangeHandler} value={this.state.brand} />
						</div>
					</div>
					<div className='col-md-3 offset-md-2 pt-5'>
						<div className='form-group'>
							<label>Unit (Enter pc for piece)</label>
							<input type='text' className='form-control' onChange={this.unitChangeHandler} value={this.state.unit} />
						</div>
						<div className='form-group'>
							<label>Price</label>
							<input type='text' className='form-control' onChange={this.priceChangeHandler} value={this.state.price} />
						</div>
						{storeDetails}
						<div className='form-group'>
							<label>Upload an image for the product</label>
							<input type='file' onChange={this.fileUploadChangeHandler} value={this.state.filename} />
						</div>
					</div>
				</div>
				<div className='row'>
					<div className='col-md-6 offset-md-3'>
						<p className='text-success text-center'>{this.state.successMsg}</p>
						<p className='text-danger text-center'>{this.state.errMsg}</p>
						{action}
					</div>
				</div>
			</div>
		)
	}
}
//export StoreInfoComponent Component
export default StoreInfoComponent
