import React, { Component } from 'react';
import Header from '../../Common/header';
import Navigation from '../../Common/navbar';
import ItemCard from '../../Common/itemCard';
import ProductInfoComponent from './productInfoComponent';

class Home extends Component {

    constructor() {
        super()
        this.state = {
            allProducts: []
        }
    }

    render() {

        let allProducts = []
        if (this.state.allProducts.length === 0) {
            allProducts.push(
                <h2 className="font-weight-light text-center mt-5">Oops! Looks like you do not have any products in this store at the moment</h2>
            )
            allProducts.push(
                <div className="mb-5">
                    <ProductInfoComponent storeId={this.props.match.params.storeId} />
                </div>
            )
        } else {
            let tempContainer = []
            for (var index = 0; index < 9; index++) {
                tempContainer.push(
                    <div className="col-md-6">
                        <ItemCard isAdmin={true} storeId={this.props.match.params.storeId} />
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