import React, { Component } from 'react';
import Header from '../../Common/header';
import Navigation from '../../Common/navbar';
import ItemCard from '../../Common/itemCard';
import BrowseStores from './browseStores';
import axios from 'axios';
import constants from '../../../utils/constants';

class AddToCart extends Component {

    constructor() {
        super()
        this.state = {
            allProducts: [],
            isFetched: false,
            searchValue: "",
            searchSKU: true,
            currentStoreID: null
        }
    }

    componentDidMount() {
        axios.get(`${constants.BACKEND_SERVER.URL}/orders/activeStoreInCart?userId=${localStorage.getItem('275UserId')}`)
            .then((response) => {
                this.setState({
                    currentStoreID: response.data.id
                })
                this.viewAllProducts(response.data.id)
            })

    }

    updateCurrentStore = (id) => {
        this.setState({
            currentStoreID: id
        })
    }

    clearSearch = () => {
        this.viewAllProducts(this.state.currentStoreID)
        this.setState({
            searchValue: "",
            searchSKU: true,
        })
    }

    viewAllProducts = (storeId) => {
        if (storeId) {
            axios.get(`${constants.BACKEND_SERVER.URL}/product/get/all?storeId=${storeId}`)
                .then((response) => {
                    this.setState({
                        allProducts: response.data,
                        isFetched: true
                    })
                })
                .catch(() => {
                    this.setState({
                        isFetched: true
                    })
                })
        }
    }

    searchValueChangeHandler = (e) => {
        this.setState({
            searchValue: e.target.value
        })
    }

    searchTypeChangeHandler = (e) => {
        this.setState({
            searchSKU: e.target.value
        })
    }

    searchProducts = () => {
        if (this.state.currentStoreID) {
            if (this.state.searchSKU === true) {
                axios.get(`${constants.BACKEND_SERVER.URL}/product/search/all/?storeId=${this.state.currentStoreID}&SKU=${this.state.searchValue}`)
                    .then((response) => {
                        this.setState({
                            allProducts: response.data
                        })
                    })
            } else {
                axios.get(`${constants.BACKEND_SERVER.URL}/product/search/all/?storeId=${this.state.currentStoreID}&name=${this.state.searchValue}`)
                    .then((response) => {
                        this.setState({
                            allProducts: response.data
                        })
                    })
            }
        }
    }

    render() {

        let allProducts = []
        if (this.state.allProducts.length === 0) {
            if (this.state.currentStoreID === null) {
                allProducts.push(<BrowseStores updateStore={this.updateCurrentStore} refresh={this.viewAllProducts} />)
            } else if (this.state.isFetched === true) {
                allProducts.push(
                    <div className="row pt-5">
                        <div className="col-md-2 offset-md-1">
                            <select className="form-control" onChange={this.searchTypeChangeHandler} value={this.state.searchSKU} >
                                <option value={true}>SKU</option>
                                <option value={false}>Product Name</option>
                            </select>
                        </div>
                        <div className="col-md-4">
                            <input type="text" className="form-control" placeholder="Product Name" value={this.state.searchValue} onChange={this.searchValueChangeHandler} />
                        </div>
                        <div className="col-md-2">
                            <button className="btn btn-success w-100" onClick={this.searchProducts}>Search</button>
                        </div>
                        <div className="col-md-2">
                            <button className="btn btn-warning w-100" onClick={this.clearSearch}>Clear Search</button>
                        </div>
                    </div>
                )
                allProducts.push(
                    <h2 className="font-weight-light text-center mt-5">Oops! Looks like there are no products in this store at the moment</h2>
                )
            }
        } else {
            allProducts.push(
                <div className="row pt-5">
                    <div className="col-md-2 offset-md-1">
                        <select className="form-control" onChange={this.searchTypeChangeHandler} value={this.state.searchSKU} >
                            <option value={true}>SKU</option>
                            <option value={false}>Product Name</option>
                        </select>
                    </div>
                    <div className="col-md-4">
                        <input type="text" className="form-control" placeholder="Product Name" value={this.state.searchValue} onChange={this.searchValueChangeHandler} />
                    </div>
                    <div className="col-md-2">
                        <button className="btn btn-success w-100" onClick={this.searchProducts}>Search</button>
                    </div>
                    <div className="col-md-2">
                        <button className="btn btn-warning w-100" onClick={this.clearSearch}>Clear Search</button>
                    </div>
                </div>
            )
            let tempContainer = []
            for (var index in this.state.allProducts) {
                tempContainer.push(
                    <div className="col-md-6">
                        <ItemCard storeId={this.props.match.params.storeId} productObj={this.state.allProducts[index]} showQuantity={true} />
                    </div>
                )
                if ((index + 1) % 2 === 0) {
                    allProducts.push(
                        <div className="row mt-2">
                            {tempContainer}
                        </div>
                    )
                    tempContainer = []
                }
            }
            allProducts.push(
                <div className="row mt-2">
                    {tempContainer}
                </div>
            )
        }

        return (
            <div>
                <Header />
                <Navigation />
                <div className="pl-5 pr-5">

                    {allProducts}

                </div>
            </div>
        )
    }
}
//export AddToCart Component
export default AddToCart;