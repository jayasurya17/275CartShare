import React, { Component } from 'react';
import Header from '../../Common/header';
import Navigation from '../../Common/navbar';
import ItemCard from '../../Common/itemCard';
import BrowseStores from './browseStores';
import axios from 'axios';


class AddToCart extends Component {

    constructor() {
        super()
        this.state = {
            allProducts: [],
            resetProducts: [],
            isFetched: false,
            searchValue: "",
            searchSKU: true,
            currentStoreID: null,
            searchResult: false,
            isStoreActive: true
        }
    }

    componentDidMount() {
        axios.get(`/orders/activeStoreInCart?userId=${localStorage.getItem('275UserId')}`)
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
        this.setState({
            allProducts: this.state.resetProducts,
            searchValue: "",
            searchResult: false
        })
    }

    viewAllProducts = (storeId) => {
        if (storeId) {
            axios.get(`/product/get/all?storeId=${storeId}`)
                .then((response) => {
                    this.setState({
                        allProducts: response.data,
                        resetProducts: response.data,
                        isFetched: true,
                        searchValue: "",
                        searchResult: false,
                    })
                    if (response.status === 204) {
                        this.setState({
                            isStoreActive: false
                        })
                    }
                })
                .catch(() => {
                    this.setState({
                        isFetched: true,
                        searchValue: "",
                        searchResult: false,
                    })
                })
        }
    }

    searchProducts = (e) => {
        var filer = []
        for (var product of this.state.resetProducts) {
            if (String(product.sku).startsWith(e.target.value) || product.productName.startsWith(e.target.value)) {
                filer.push(product)
            }
        }
        this.setState({
            allProducts: filer,
            searchValue: e.target.value,
            searchResult: true
        })
    }

    render() {

        let allProducts = []
        if (this.state.allProducts.length === 0) {
            if (this.state.currentStoreID === null) {
                allProducts.push(<BrowseStores updateStore={this.updateCurrentStore} refresh={this.viewAllProducts} />)
            } else if (this.state.isFetched === true) {
                if (this.state.isStoreActive === false) {
                    allProducts.push(
                        <h2 className="font-weight-light text-center mt-5">The store you have been shopping from has been deleted.</h2>
                    )
                    allProducts.push(
                        <h2 className="font-weight-light text-center mt-5">Please clear the cart to shop from a different store.</h2>
                    )
                } else {
                    allProducts.push(
                        <div className="row pt-5">
                            <div className="col-md-6 offset-md-2">
                                <input type="text" className="form-control" value={this.state.searchValue} onChange={this.searchProducts} placeholder="Search by SKU or name" />
                            </div>
                            <div className="col-md-2">
                                <button className="btn btn-warning w-100" onClick={this.clearSearch}>Clear Search</button>
                            </div>
                        </div>
                    )
                    if (this.state.searchResult === false) {
                        allProducts.push(
                            <h2 className="font-weight-light text-center mt-5">Oops! Looks like there are no products in this store at the moment</h2>
                        )
                    } else {
                        allProducts.push(
                            <h2 className="font-weight-light text-center mt-5">Oops! There are no products matching your search</h2>
                        )
                    }
                }   

            }
        } else {
            allProducts.push(
                <div className="row pt-5">
                    <div className="col-md-6 offset-md-2">
                        <input type="text" className="form-control" value={this.state.searchValue} onChange={this.searchProducts} placeholder="Search by SKU or name" />
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