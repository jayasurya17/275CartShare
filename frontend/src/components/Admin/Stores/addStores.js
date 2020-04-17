import React, { Component } from 'react';
import Header from '../../Common/header';
import Navigation from '../../Common/navbar';
import AddStoreComponent from './storeInfoComponent';

class AddStores extends Component {

    render() {

        return (
            <div>
                <Header isAdmin={true} />
                <Navigation isAdmin={true} />
                
                <AddStoreComponent />
            </div>
        )
    }
}
//export AddStores Component
export default AddStores;