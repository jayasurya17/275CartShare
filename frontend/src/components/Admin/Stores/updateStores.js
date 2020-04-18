import React, { Component } from 'react';
import Header from '../../Common/header';
import Navigation from '../../Common/navbar';
import StoreInfoComponent from './storeInfoComponent';

class AddStores extends Component {

    render() {

        return (
            <div>
                <Header isAdmin={true} />
                <Navigation isAdmin={true} />
                
                <StoreInfoComponent storeId={10}/>
            </div>
        )
    }
}
//export AddStores Component
export default AddStores;