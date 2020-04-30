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
            isFetched: false
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

                    {allProducts}

                </div>
            </div>
        )
    }
}
//export Home Component
export default Home;