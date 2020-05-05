import React, { Component } from 'react';
import Header from '../../Common/header';
import Navigation from '../../Common/navbar';
import ItemCard from '../../Common/itemCard';
import ProductInfoComponent from './productInfoComponent';
import axios from 'axios';


class Home extends Component {

    constructor() {
        super()
        this.state = {
            allProducts: [],
            isFetched: false,
            isStoreActive: true
        }
    }

    componentDidMount() {
        this.getAllProducts();
    }

    getAllProducts = () => {
        axios.get(`/product/get/all?storeId=${this.props.match.params.storeId}`)
            .then((response) => {
                console.log(response)
                this.setState({
                    allProducts: response.data,
                    isFetched: true
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
                    isStoreActive: false
                })
            })
    }

    render() {

        let allProducts = []
        if (this.state.allProducts.length === 0) {
            if (this.state.isFetched === true) {
                if (this.state.isStoreActive === false) {
                    allProducts.push(
                        <h2 className="font-weight-light text-center mt-5">Oops! Looks like this store does not exist or has been deleted</h2>
                    )
                } else {
                    allProducts.push(
                        <h2 className="font-weight-light text-center mt-5">Oops! Looks like you do not have any products in this store at the moment</h2>
                    )
                    allProducts.push(
                        <div className="mb-5">
                            <ProductInfoComponent storeId={this.props.match.params.storeId} getAllProducts={this.getAllProducts}/>
                        </div>
                    )
                }
            }
        } else {
            let tempContainer = []
            for (var index in this.state.allProducts) {
                tempContainer.push(
                    <div className="col-md-6">
                        <ItemCard isAdmin={true} storeId={this.props.match.params.storeId} productObj={this.state.allProducts[index]} />
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

                    {allProducts}

                </div>
            </div>
        )
    }
}
//export Home Component
export default Home;