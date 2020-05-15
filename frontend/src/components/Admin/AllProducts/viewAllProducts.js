import React, { Component } from 'react';
import Header from '../../Common/header';
import Navigation from '../../Common/navbar';
import ItemCard from '../../Common/itemCard';
import ProductInfoComponent from '../Products/productInfoComponent';
import axios from 'axios';


class BrowseProducts extends Component {

    constructor() {
        super()
        this.state = {
            allProducts: [],
            resetProducts: [],
            isFetched: false,
            storeOrSKU: "",
            name: "",
            searchResult: false
        }
    }

    componentDidMount() {
        this.getallProducts()
    }

    getallProducts = () => {
        axios.get(`/product/get/all`)
            .then((response) => {
                this.setState({
                    allProducts: response.data,
                    resetProducts: response.data,
                    isFetched: true
                })
            })
            .catch(() => {
                this.setState({
                    isFetched: true
                })
            })
    }

    filterByStoreOrSku = (e) => {
        var temp = []
        for (var product of this.state.resetProducts) {
            if ((String(product.store.id).startsWith(e.target.value) || String(product.sku).startsWith(e.target.value)) && product.productName.startsWith(this.state.name)) {
                temp.push(product)
            }
        }
        this.setState({
            allProducts: temp,
            [e.target.name]: e.target.value,
            searchResult: true
        })
    }

    filterByName = (e) => {
        var temp = []
        for (var product of this.state.resetProducts) {
            if ((String(product.store.id).startsWith(this.state.storeOrSKU) || String(product.sku).startsWith(this.state.storeOrSKU)) && product.productName.startsWith(e.target.value)) {
                temp.push(product)
            }
        }
        this.setState({
            allProducts: temp,
            [e.target.name]: e.target.value,
            searchResult: true
        })
    }

    clearSearch = () => {
        this.setState({
            allProducts: this.state.resetProducts,
            storeOrSKU: "",
            name: "",
            searchResult: false
        })
    }

    render() {

        let allProducts = []
        if (this.state.allProducts.length === 0) {
            if (this.state.searchResult === true) {
                allProducts.push(
                    <h2 className="font-weight-light text-center mt-5">There are no products matching this search</h2>
                )
            } else if (this.state.isFetched === true) {
                allProducts.push(
                    <h2 className="font-weight-light text-center mt-5">Oops! Looks like you do not have any products at the moment</h2>
                )
                allProducts.push(
                    <div className="mb-5">
                        <ProductInfoComponent getAllProducts={this.getallProducts}/>
                    </div>
                )
            } else {
                allProducts.push(
                    <h2 className="font-weight-light text-center mt-5">Fetching products...</h2>
                )
            }
        } else {
            let tempContainer = []
            for (var index in this.state.allProducts) {
                tempContainer.push(
                    <div className="col-md-6">
                        <ItemCard isAdmin={true} showStore={true} productObj={this.state.allProducts[index]} getAllProducts={this.getallProducts} />
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
                <Header isAdmin={true} />
                <Navigation isAdmin={true} />
                <div className="pl-5 pr-5">
                    <div className="p-5 row">
                        <div className="col-md-3 offset-md-1">
                            <input type="text" value={this.state.storeOrSKU} name="storeOrSKU" placeholder="Store ID or SKU" onChange={this.filterByStoreOrSku} className="form-control" />
                        </div>
                        <div className="col-md-5">
                            <input type="text" value={this.state.name} name="name" placeholder="Product Name" onChange={this.filterByName} className="form-control" />
                        </div>
                        <div className="col-md-2">
                            <button className="btn btn-warning w-100" onClick={this.clearSearch}>Clear</button>
                        </div>
                    </div>
                    {allProducts}

                </div>
            </div>
        )
    }
}
//export BrowseProducts Component
export default BrowseProducts;