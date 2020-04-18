import React, { Component } from 'react';
import Header from '../../Common/header';
import Navigation from '../../Common/navbar';
import ProductInfoComponent from './productInfoComponent';

class AddStores extends Component {

    render() {

        return (
            <div>
                <Header isAdmin={true} />
                <Navigation isAdmin={true} />
                
                <ProductInfoComponent storeId={this.props.match.params.storeId} SKU={this.props.match.params.SKU} />
            </div>
        )
    }
}
//export AddStores Component
export default AddStores;