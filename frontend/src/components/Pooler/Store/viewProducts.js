import React, { Component } from 'react';
import Header from '../../Common/header';
import Navigation from '../../Common/navbar';
import ItemCard from '../../Common/itemCard';
import axios from 'axios';


class Home extends Component {

    constructor() {
        super()
        this.state = {
            allProducts: [],
            isFetched: false,
            searchValue: "",
            searchSKU: true,
        }
    }

    componentDidMount() {
        this.getAllProducts();
    }

    getAllProducts = () => {
        axios.get(`/product/get/all?storeId=${this.props.match.params.storeId}`)
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

    clearSearch = () => {
        this.getAllProducts()
        this.setState({
            searchValue: ""
        })
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
        if (this.state.searchSKU === true) {
            axios.get(`/product/search/all/?storeId=${this.props.match.params.storeId}&SKU=${this.state.searchValue}`)
                .then((response) => {
                    this.setState({
                        allProducts: response.data,
                        searchResult: true,
                    })
                })
        } else {
            axios.get(`/product/search/all/?storeId=${this.props.match.params.storeId}&name=${this.state.searchValue}`)
                .then((response) => {
                    this.setState({
                        allProducts: response.data,
                        searchResult: true,
                    })
                })
        }
    }

    render() {

        let allProducts = []
        if (this.state.allProducts.length === 0) {
            if (this.state.isFetched === true) {
                allProducts.push(
                    <h2 className="font-weight-light text-center mt-5">Oops! Looks like there are no products in this store at the moment</h2>
                )
            }
        } else {
            let tempContainer = []
            for (var index in this.state.allProducts) {
                tempContainer.push(
                    <div className="col-md-6">
                        <ItemCard storeId={this.props.match.params.storeId} productObj={this.state.allProducts[index]} />
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

                    <div className="row pt-5">
                        <div className="col-md-2 offset-md-1">
                            <select className="form-control" onChange={this.searchTypeChangeHandler} value={this.state.searchSKU} >
                                <option value={true}>SKU</option>
                                <option value={false}>Product Name</option>
                            </select>
                        </div>
                        <div className="col-md-4">
                            <input type="text" className="form-control" value={this.state.searchValue} onChange={this.searchValueChangeHandler} />
                        </div>
                        <div className="col-md-2">
                            <button className="btn btn-success w-100" onClick={this.searchProducts}>Search</button>
                        </div>
                        <div className="col-md-2">
                            <button className="btn btn-warning w-100" onClick={this.clearSearch}>Clear Search</button>
                        </div>
                    </div>

                    {allProducts}

                </div>
            </div>
        )
    }
}
//export Home Component
export default Home;